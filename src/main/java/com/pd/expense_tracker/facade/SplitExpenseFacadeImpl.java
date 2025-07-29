package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.DTO.PaymentDTO;
import com.pd.expense_tracker.model.ExpenseSplit;
import com.pd.expense_tracker.model.UserIdentifier;
import com.pd.expense_tracker.repository.ExpenseSplitRepository;
import com.pd.expense_tracker.repository.UserIdentifierRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SplitExpenseFacadeImpl {

    private final ExpenseSplitRepository expenseSplitRepository;
    private final UserIdentifierRepository userIdentifierRepository;

    public SplitExpenseFacadeImpl(ExpenseSplitRepository expenseRepo, UserIdentifierRepository userRepo) {
        this.expenseSplitRepository = expenseRepo;
        this.userIdentifierRepository = userRepo;
    }

    public ExpenseSplit addExpense(String userId, double amount) {
        UserIdentifier user = userIdentifierRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return expenseSplitRepository.save(new ExpenseSplit(user, amount));
    }

    public List<ExpenseSplit> getAllExpenses() {
        return expenseSplitRepository.findAll();
    }

    public List<PaymentDTO> calculateBalances() {
        List<ExpenseSplit> expenses = getAllExpenses();
        List<PaymentDTO> result = new ArrayList<>();

        if (expenses.isEmpty()) return result;

        Map<UserIdentifier, Double> netBalance = calculateNetBalance(expenses);

        PriorityQueue<UserBalance> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.amount, a.amount));
        PriorityQueue<UserBalance> debtors = new PriorityQueue<>(Comparator.comparingDouble(a -> a.amount));

        for (var entry : netBalance.entrySet()) {
            double amt = entry.getValue();
            if (amt > 0) creditors.add(new UserBalance(entry.getKey(), amt));
            else if (amt < 0) debtors.add(new UserBalance(entry.getKey(), amt));
        }

        result = calculateFinalPayment(creditors, debtors);

        return result;
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    static class UserBalance {
        UserIdentifier user;
        double amount;

        public UserBalance(UserIdentifier user, double amount) {
            this.user = user;
            this.amount = amount;
        }
    }

    private Map<UserIdentifier, Double> calculateNetBalance(List<ExpenseSplit> expenses){

        Map<UserIdentifier, Double> userTotal = new HashMap<>();
        for (ExpenseSplit e : expenses) {
            UserIdentifier userId = e.getUser();
            userTotal.put(userId, userTotal.getOrDefault(userId, 0.0) + e.getAmount());
        }

        double total = userTotal.values().stream().mapToDouble(Double::doubleValue).sum();
        double share = total / userTotal.size();

        Map<UserIdentifier, Double> netBalance = new HashMap<>();
        for (UserIdentifier user : userTotal.keySet()) {
            netBalance.put(user, round(userTotal.get(user) - share));
        }
        return netBalance;
    }

    private List<PaymentDTO> calculateFinalPayment(PriorityQueue<UserBalance> creditors, PriorityQueue<UserBalance> debtors){
        List<PaymentDTO> result = new ArrayList<>();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            UserBalance creditor = creditors.poll();
            UserBalance debtor = debtors.poll();

            double settled = Math.min(creditor.amount, -debtor.amount);
            settled = round(settled);

            result.add(new PaymentDTO(
                    debtor.user.getName(), creditor.user.getName(), settled
            ));
            creditor.amount -= settled;
            debtor.amount += settled;

            if (round(creditor.amount) > 0) creditors.add(creditor);
            if (round(debtor.amount) < 0) debtors.add(debtor);
        }
        return  result;
    }
}
