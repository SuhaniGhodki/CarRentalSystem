import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Car{
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    //using constructor as method needs to be invoked and constructor calls itself
    public Car(String carId, String brand, String model, double basePricePerDay){
        // will assign all the perticular values of the data members with the help of this keyword
        this.carId= carId;
        this.brand=brand;
        this.model=model;
        this.basePricePerDay=basePricePerDay;
        this.isAvailable=true; //new to the list hence ready for renting
    }
    //using getter method so that we can check the private data members inside the class

    public String getCarId(){
        return carId;
    }

    public String getBrand(){
        return brand;
    }

    public String getModel(){
        return model;
    }

    public double calculatePrice(int rentalDays){
        return basePricePerDay * rentalDays;
    }
    public boolean isAvailable(){
        return isAvailable;
    }

    public void rent(){
        isAvailable= false; //got rented so not available
    }

    public void returnCar(){
        isAvailable=true; //available again
    }
}

class Customer{
    private String customerId;
    private String name;

    public Customer(String customerId, String name){
        //using this to assign values to data members  
        this.customerId=customerId;
        this.name=name;
    }
    //using getter method to access the data members 
    public String getCustomerId(){
        return customerId;
    }
    public String getName(){
        return name;
    }
}

class Rental{
    // using class as a datatype here as the car is an object that contains multiple data members
    // and rather than using string which might give errors, we are using the type of datatype which can access this object.
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer , int days){
        this.car=car;
        this.customer=customer;
        this.days=days;
    }

    public Car getCar(){
        return car;
    }
    public Customer getCustomer(){
        return customer;
    }
    public int getDays(){
        return days;
    }
}

class CarRentalSystem{
    //Using arraylist for this project because unlike arrays, it gives undefinite storage for your data 
    //will store car's object 
    private List<Car> cars;
    //will store the cars object and its data members 
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem(){
        cars= new ArrayList<>();
        customers = new ArrayList<>();
        rentals= new ArrayList<>();
    }
    public void addCar(Car car){
        cars.add(car);
    }
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    public void rentCar(Car car, Customer customer, int days){
        if(car.isAvailable()){
            car.rent();
            rentals.add(new Rental(car, customer, days));
        }else{
            System.out.println("Car is not available for rent");
        }
    }
    public void returnCar(Car car){
        Rental rentalToRemove=null; //at first we don't have anything to return
        for(Rental rental: rentals){  //using for each loop to iterate all the car models
            if(rental.getCar()==car){  //checking if there exist the same car in the rental arraylist
                rentalToRemove=rental; //if true then this variable saves the data of the rental car
                break;
            }
        }
        if(rentalToRemove != null){  
            rentals.remove(rentalToRemove);  //removing from rented list of cars 
            car.returnCar(); //making it available for rent again
        }else{
            System.out.println("The car was not rented");
        }
    }

    public void menu(){
        Scanner sc= new Scanner(System.in);
        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice= sc.nextInt();
            sc.nextLine();

            if(choice==1){
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = sc.nextLine();

                System.out.println("\nAvailable Cars:");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }
                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = sc.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = sc.nextInt();
                sc.nextLine(); // Consume newline

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }
                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = sc.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\nCar rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }
            } 
            else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = sc.nextLine();

                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && ! car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } 
                else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } 
            else if (choice == 3) {
                break;
            } 
            else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
            }
        }
    }

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0); // Different base price per day for each car
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}
