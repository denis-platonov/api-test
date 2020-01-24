## API Demo Challange

#### Project Setup
1. Clone this project.
2. Setup the project in your IDE.
3. From command line run mvn clean install -U -DskipTests
5. Make sure you can run the DemoTest

#### Expectations
We will be evaluating
1. Quality of test cases
2. Variety  of testing types (examples: boundary, happy path, negative, etc)
3. Code structure and organization
4. Naming conventions
5. Code readability
6. Code modularity


#### Excercise
1. Review the spec in the root directory, PizzaAPIReferenceDoc.  API endpoints for this excercise can be found here
   https://my-json-server.typicode.com/sa2225/demo/
2. In the Read me file, write up all of the test cases you think are necessary to test the endpoints defined in the provided spec.
3. Code up a few examples of 
  - order get call including response validation
  - order post call
4. When complete please check your code into a public git repo

#### Test Cases
Toppings:
 1.  GET toppings (happy path) - validate against schema and values as well
 2.  GET toppings/5 - returns a topping by ID (not documented)
 3.  GET toppings/-1 - equivalence class negative (not documented)
 4.  GET toppings/0 - boundary (not documented)
 5.  GET toppings/0 - boundary (not documented)
 6.  GET toppings/11 - boundary (not documented)
 7.  GET toppings/12 - boundary (not documented)
 8.  GET toppings?id=1 - returns a topping by ID using query (not documented)
 9.  GET toppings?id=a - returns a topping by ID using query (not documented) negative letters
 10. GET toppings?id=#% - returns a topping by ID using query (not documented) negative special characters
 11. GET toppings?id= - returns a topping by ID using query (not documented) negative no value
 12. GET toppings?id= - returns a topping by ID using query (not documented) negative no value
 13. GET toppings?name=Olives - returns a topping by name using query (not documented) positive
 14. GET toppings?name= by name using query, including letters, characters, empty value etc
 15. POST toppings (happy path) - validate against schema and values as well 204
 16. POST toppings (existing topping) negative 405
 17. POST toppings (empty body, body as array, as integer, as incorrect object) negative
 18. POST toppings (name with no value, as integer, as array, as object, special characters, name is empty, name is too long, name capitalized, uppercase) negative
 19. POST toppings (SQL injection) Security
 20. POST toppings (add/modify new id) negative
 21. POST toppings (change content type and send incorrect types) negative
 22. POST toppings (send a payload in a query like GET, play around with different types of body) negative
 22. POST measure basic performance (?) in ms
 23. DELETE existing topping - happy path 204
 24. DELETE with negative id, id=0, id more than existing, empty id - boundary\equivalence class negative
 25. DELETE with id as special characters, letters
 26. etc

#### Notes
1. I've noticed a logger that is not being used - could be hooked up to a listener (if any available) to generate automatic logs or used manually
2. I've also noticed RestAssured schema validator in pom.xml and implemented appropriate validations with its help. I generated a json schema based on response but in real world the contract should be provided
3. I'd also look at parallel execution even though API tests are quite fast. Total execution time can go drastically up when the number of test cases grows in regression suite
4. Having multiple assertions within the same test case is questionable. So I'd also investigate soft assertions, or refactor the test cases, so they are atomic
5. Moved BaseURI to BaseTest class for code reuse but would consider to pass it from command line / properties, so it is environment agnostic
6. Expected results are hardcoded but could be pulled from a database or any other source
7. I'm not sure about reqs of this project but HTTPS is not enforced, so regular HTTP requests can be executed and intercepted potentially :)
8. There are hundreds of test cases for this project as well as hundreds of bugs )
9. It is already 190 lines of code in OrdersTest class, so I'd consider restructuring them by creating sub-folders for every end-point and creating corresponding classes to separate out GET\POST\DELETE type of test cases
10. Didn't touch .gitignore, so it is messy

PS: There are some typos in the README doc itself, like "API Demo Challange" or "Excercise"
