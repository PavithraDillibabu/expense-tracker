Expense tracker system that allows users to track, categorize, and forecast their expenses, split costs with others, and monitor budgets. Supports real-time data processing and integrates currency conversion for global use.

Features:
  1. Expense Tracking    : Manages CRUD operations for expenses and publishes an event after each creation to track budget.
  2. Expense Splitting   : Manages splitting expenses among users by calculating net balances and determining payments needed to settle debts. It efficiently match debtors and creditors for minimal transactions.
  3. Category Management : Manages CRUD operations for expense categories, search, and bulk creation. It also provides insights like category usage counts and top-used categories for reporting purposes.
  4. Forecasting         : Linear regression on recent expense data to predict the total expense for the next month. It returns the forecasted amount along with a labeled month for easy user interpretation.
  5. Currency Conversion : Converts an expense amount from one currency to another using real-time exchange rates and enriches the conversion with user location data based on their IP.
  6. Budget Monitoring   : Kafka consumer listens for expense events and checks if the user’s total spending exceeds their budget. Future Improvement needs Mail/Push notification.

Tech Stack:
  1. Programming language : Java 		       	            
  2. Backend framework    : Spring Boot    	              
  3. Spring Security	    : OAuth2   
  4. Event streaming   	  : Apache Kafka            
  5. ORM layer            : Hibernate		    
  6. Database		     	    : H2            
  7. API documentation    : Swagger      	
  8. Containerization     : Docker
