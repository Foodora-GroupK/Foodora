# MyFoodora Test Scenarios Report

## testScenario1.txt Description

This test scenario file tests the complete functionality of the MyFoodora system through a series of integrated operations. Here's what the scenario tests:

1. **System Initialization and User Management**
   - System setup with specified number of restaurants, customers, and couriers
   - Manager login functionality
   - User registration for different roles (restaurants, customers, couriers)
   - Duplicate registration handling

2. **Restaurant Operations**
   - Menu management (adding dishes)
   - Meal creation and modification
   - Special offer management
   - Menu item categorization (starter, main, dessert)
   - Food type handling (standard, vegetarian, gluten-free)

3. **Order Processing**
   - Order creation by customers
   - Adding items to orders
   - Order completion and payment
   - Date handling for orders

4. **Courier Management**
   - Courier registration
   - Duty status management (on/off duty)
   - Delivery assignment

5. **System Administration**
   - Policy management (delivery and profit policies)
   - System statistics and reporting
   - Customer fidelity card management

6. **Error Handling**
   - Invalid login attempts
   - Duplicate registration attempts
   - Invalid operations based on user roles
   - Missing or invalid parameters

The test scenario follows a logical flow:
1. First sets up the system
2. Registers necessary users
3. Performs restaurant operations
4. Processes customer orders
5. Manages courier operations
6. Tests manager functionalities
7. Verifies error handling

This comprehensive test ensures that all major components of the system work together correctly and that the system properly handles both valid operations and error conditions. 