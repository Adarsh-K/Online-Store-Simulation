import java.util.*;
class Account{
	/*
		Used for the Account Balance of the Company
	*/
	public float Balance;
}
class Item{
	/*
		An item by Merchant
	*/
	private static int Counter=1; // "static" For "Unique" Item Code
	// "final" there shouldn't be any changes made to the following (all with "final") parameters 
	private final int Code;
	private final Merchant _Merchant;
	private final String Name;
	private float Price;
	private int Quantity;
	private final String Category;
	private String Offer="None"; // Initially No Offers
	public Item(Merchant _Merchant,String Name, float Price, int Quantity, String Category){ 
		this.Code=Counter++;
		this._Merchant=_Merchant;
		this.Name=Name;
		this.Price=Price;
		this.Quantity=Quantity;
		this.Category=Category;
		this.display_Details();
		// Invoke add_to_Categories after creating an Item Obj.
	}
	// Getters
	public int get_Code(){
		return this.Code;
	}
	public Merchant get_Merchant(){
		return this._Merchant;
	}
	public String get_Merchant_Name(){
		return this._Merchant.get_Name();
	}
	public String get_Name(){
		return this.Name;
	}
	public float get_Price(){
		return this.Price;
	}
	public int get_Quantity(){
		return this.Quantity;
	}
	public String get_Category(){
		return this.Category;
	}
	public String get_Offer(){
		return this.Offer;
	}
	// Setters
	public void set_Price(float Price){
		this.Price=Price;
	}
	public void set_Quantity(int Quantity){
		this.Quantity=Quantity;
	}
	public void add_to_Categories(Categories all_Categories){ // Pass All_Categories in all_Categories
		/*
			Adds the item to All Categories using method add_Item() in Categories Class
		*/
		all_Categories.add_Item(this);
	}
	public void add_Offer(int Offer_option){
		if(Offer_option==1){
			this.Offer="buy one get one";
		}
		else{
			this.Offer="25% off";
		}
		this.display_Details();
	}
	public void display_Details(){
		/*
			Displays the item's detail in the required format
		*/
		System.out.printf("%d %s %f %d %s %s%n", this.Code, this.Name, this.Price, this.Quantity,
			this.Offer, this.Category);
	}
}
class Items{
	/*
		Used to store item with it's quantity
	*/	
	private final Item _Item;
	private final int Quantity;
	public Items(Item _Item, int Quantity){
		this._Item=_Item;
		this.Quantity=Quantity;
	}
	// Getters
	public Item get_Item(){
		return this._Item;
	}
	public int get_Quantity(){
		return this.Quantity;
	}
	public float get_total_Price(){
		/*
			Returns total price of all units of the item considering the offers associated with it
		*/
		float total_Price=this._Item.get_Price()*Quantity;
		int new_Quantity=0;
		if(this._Item.get_Offer().equals("None")){

		}
		else{
			if(this._Item.get_Offer().equals("buy one get one")){
				new_Quantity=(int) Math.ceil(Quantity/2.0);
				total_Price=this._Item.get_Price()*new_Quantity;
			}
			else{
				total_Price*=0.75; // 25% off
			}
		}
		return total_Price;
	}
}
class Categories{
	/*
		Contains all items soretd by categories
	*/
	public ArrayList<ArrayList<Item>> All_categories;
	public Categories(){
		this.All_categories=new ArrayList<ArrayList<Item>>();
	}
	public void add_Item(Item new_Item){
		/*
			Adds an item to respective category
		*/
		// Checks if the category exists and adds to it
		for(int i=0;i<this.All_categories.size();i++){
			String temp_Category=this.All_categories.get(i).get(0).get_Category();
			if(temp_Category.equals(new_Item.get_Category())){
				this.All_categories.get(i).add(new_Item);
				return;
			}
		}
		// If such a category doesn't exist, makes a new one  
		ArrayList<Item> new_Category=new ArrayList<Item>();
		new_Category.add(new_Item);
		this.All_categories.add(new_Category);
	}
	public void display_Categories(){
		for(int i=0;i<this.All_categories.size();i++){
			System.out.printf("%d) %s%n", i+1, this.All_categories.get(i).get(0).get_Category());
		}
	}
	public void display_all_Items(int category_Option){
		/*
			Displays all items of a category
		*/
		for(int i=0;i<this.All_categories.get(category_Option-1).size();i++){
			Item temp_Item=this.All_categories.get(category_Option-1).get(i);
			temp_Item.display_Details();
		}
	}
}
interface User{
	/*
		Implemented by Class Merchant and Customer
	*/ 
	// Scanner Class obj. scan passed as an arguement in following methods
	public void choose_Query(Scanner scan, Categories _Categories, Account Company_Balance);
}
class User_Menu{
	/*
		Selects Menu of either Merchant or Customer depending on parameter passed (Polymorphism)
	*/
	public void select_Menu(Scanner scan, User _User, Categories _Categories, Account Company_Balance){
		// Polymorphism here in User parameter!!
		_User.choose_Query(scan, _Categories, Company_Balance);
	}
}
class Merchant implements User{
	private final String Name;
	private final String Address;
	private float Contribution;
	private int Reward;
	private ArrayList<Item> All_items;
	private boolean now_Reward;
	public Merchant(String Name, String Address){
		this.Name=Name;
		this.Address=Address;
		this.All_items=new ArrayList<Item>();
	}
	public String get_Name(){
		return this.Name;
	}
	public void inc_Contribution(float Update){
		/*
			Merchant's contribution to the Company
		*/
		this.Contribution+=Update;
		this.get_Reward();
	}
	public void add_Item(Item new_Item){
		this.All_items.add(new_Item);
	}
	public void edit_Item(Item _Item, float Price, int Quantity){
		_Item.set_Price(Price);
		_Item.set_Quantity(Quantity);
		_Item.display_Details();  
	}
	public void add_Offer(Item _Item, int Offer_option){
		_Item.add_Offer(Offer_option);
	}
	public void get_Reward(){
		/*
			Checks eligiblity for the Reward
		*/
		if(this.now_Reward){
			this.Reward++;
			this.now_Reward=false;
		}
	}
	public void display_Reward(){
		System.out.println(this.Reward);
	}
	public void display_all_Items(){
		for(int i=0;i<this.All_items.size();i++){
			this.All_items.get(i).display_Details();
		}
	}
	public void display_Details(){
		System.out.printf("Name: %s%nAddress: %s%nTotal contribution to company's account: %f%n",
			this.Name,this.Address, this.Contribution);
	}
	@Override
	public void choose_Query(Scanner scan, Categories _Categories, Account Company_Balance){
		// Overrides the choose_Query() declared methods of interface User!!
		while(true){
			// To stay on the Merchant Menu until exits
			System.out.printf("Welcome %s%nMerchant Menu%n", this.Name);
			System.out.printf("1) Add item%n2) Edit item%n3) Search by category%n4) Add offer%n5) Rewards won%n6) Exit%n");
			int query=Integer.valueOf(scan.next());
			if(query==1){
				// Add items 
				System.out.printf("Enter item details%nitem name:%n");
				String new_Name=scan.next();
				System.out.printf("item price:%n");
				float new_Price=Float.valueOf(scan.next());
				System.out.printf("item quantity:%n");
				int new_Quantity=Integer.valueOf(scan.next());
				System.out.printf("item category:%n");
				String new_Category=scan.next();
				Item new_Item=new Item(this, new_Name, new_Price, new_Quantity, new_Category);
				if(this.All_items.size()+1<=(10+this.Reward)){
					// Checks if maximum item limit has been reached
					new_Item.add_to_Categories(_Categories); // Add to Categories 
					this.add_Item(new_Item); // Adds the item to all items he/she sells
				}
				else{
					System.out.println("Error: Can't add more items!");
					// Quit the Option!
				}
			}
			else if(query==2){
				// Edit items
				System.out.println("choose item by code");
				this.display_all_Items();
				if(this.All_items.size()==0){
					// If no items, used later as well
					System.out.println("No item!");
					continue;
				}
				int item_Option=Integer.valueOf(scan.next());
				System.out.println("Enter edit details");
				for(int i=0;i<this.All_items.size();i++){
					if(item_Option==this.All_items.get(i).get_Code()){
						Item temp_Item=this.All_items.get(i);
						System.out.println("item price:");
						temp_Item.set_Price(Float.valueOf(scan.next()));
						System.out.println("item quantity:");
						temp_Item.set_Quantity(Integer.valueOf(scan.next()));
						temp_Item.display_Details();
						break; // No "Error Handling" req. here
					}
				}
			}
			else if(query==3){
				// Searching 
				System.out.println("Choose a category");
				_Categories.display_Categories();
				if(_Categories.All_categories.size()==0){
					System.out.println("No category!");
					continue;
				}
				int category_Option=Integer.valueOf(scan.next());
				_Categories.display_all_Items(category_Option);
			}
			else if(query==4){
				// Add offers
				System.out.println("choose item by code");
				this.display_all_Items();
				if(this.All_items.size()==0){
					System.out.println("No item!");
					continue;
				}
				int item_Option=Integer.valueOf(scan.next());
				for(int i=0;i<this.All_items.size();i++){
					if(item_Option==this.All_items.get(i).get_Code()){
						Item temp_Item=this.All_items.get(i);
						System.out.printf("choose offer%n1) buy one get one%n2) 25%% off%n");
						int offer_Option=Integer.valueOf(scan.next());
						temp_Item.add_Offer(offer_Option);
						break; // No "Error Handling" req. here
					}
				}
			}
			else if(query==5){
				// Print Rewards won
				this.display_Reward();
			}
			else{
				// Exit
				return;
			}
		}
	}
}
class Customer implements User{
	private final String Name;
	private final String Address;
	private int Orders;
	private float Main_Acc;
	private float Reward_Acc;
	private float Reward;
	private ArrayList<ArrayList<Items>> All_orders;
	private ArrayList<Items> Cart;
	public Customer(String Name, String Address){
		this.Name=Name;
		this.Address=Address;
		this.Main_Acc=100;
		this.All_orders=new ArrayList<ArrayList<Items>>();
		this.Cart=new ArrayList<Items>();
	}
	public String get_Name(){
		return this.Name;
	}
	public void add_Cart(Item _Item, int Quantity){
		/*
			Adds to Cart
		*/
		Items new_Items=new Items(_Item, Quantity);
		this.Cart.add(new_Items);
	}
	public void display_Reward(){
		System.out.println(this.Reward);
	}
	public void recent_Orders(){
		/*
			Prints recent orders in required format
		*/
		int size=this.All_orders.size();
		for(int i=size-1;i>=Math.max(size-10,0);i--){
			ArrayList<Items> temp_Order=this.All_orders.get(i);
			for(int j=0;j<temp_Order.size();j++){
				Items temp_Items=temp_Order.get(j);
				System.out.printf("Bought item %s quantity: %d for Rs %f from Merchant %s%n", 
					temp_Items.get_Item().get_Name(), temp_Items.get_Quantity(), 
					temp_Items.get_total_Price(), temp_Items.get_Item().get_Merchant_Name());
			}
		}
	}
	public void get_Reward(){
		// Checks eligibility for the Reward 
		if(this.Orders!=0&&this.Orders%5==0){
			this.Reward+=10;
			this.Reward_Acc+=10;
		}
	}
	public boolean validate_Purchase(Item _Item, int Quantity){
		/*
			Validates a Purchase considering Availability of Stock & Account Balance
		*/
		float total_Price=_Item.get_Price()*Quantity;
		int new_Quantity=0;
		if(_Item.get_Offer().equals("None")){

		}
		else{
			if(_Item.get_Offer().equals("buy one get one")){
				new_Quantity=(int) Math.ceil(Quantity/2.0);
				total_Price=_Item.get_Price()*new_Quantity;
			}
			else{
				total_Price*=0.75;
			}
		}
		total_Price*=1.005;
		if(Quantity>_Item.get_Quantity()){
			System.out.println("Error: Out of Stock!");
			return false;
		}
		else{
			if(total_Price>(this.Main_Acc+this.Reward_Acc)){
				System.out.println("Error: Out of Money!");
				return false;
			}
			else{
				return true;
			}
		}
	}
	public float cost(Item _Item, int Quantity){
		/*
			Returns total cost of the purchase after it's validated considering Offers
		*/  
		float total_Price=_Item.get_Price()*Quantity;
		if(_Item.get_Offer().equals("None")){

		}
		else{
			if(_Item.get_Offer().equals("buy one get one")){
				int new_Quantity=(int) Math.ceil(Quantity/2.0);
				total_Price=_Item.get_Price()*new_Quantity;
			}
			else{
				total_Price*=0.75;
			}
		}
		return total_Price;
	}
	public void post_Purchase(Item temp_Item, int Quantity, Account Company_Balance){
		/*
			Performs essential tasks after request of a valid purcase, like:
			1. Deducts amount from the Accounts
			2. Adds Transfer Fee to the Company's Account
			3. Increments the contribution of the item's Merchant to the Company
			4. Reduces the available stock of the item
		*/  
		float total_Price=this.cost(temp_Item, Quantity); 
		total_Price*=1.005; // Transaction Fee
		if(this.Main_Acc-total_Price>0){
			this.Main_Acc-=total_Price;
		}
		else{
			this.Reward_Acc-=(total_Price-this.Main_Acc);
			this.Main_Acc=0;
		}
		Company_Balance.Balance+=((total_Price/1.005)*0.01); // Transaction Fee to Company
		float total_Price_2=this.cost(temp_Item, Quantity); 
		total_Price_2*=0.005;
		temp_Item.get_Merchant().inc_Contribution(total_Price_2); // Add to Merchant's Contribution
		temp_Item.set_Quantity(temp_Item.get_Quantity()-Quantity); // Decrement Quantity of that Item
	}
	public void display_Details(){
		System.out.printf("Name: %s%nAddress: %s%nNumber of orders placed via the application: %d%n",
			this.Name,this.Address, this.Orders);
	}
	@Override
	public void choose_Query(Scanner scan, Categories _Categories, Account Company_Balance){
		// Overrides the choose_Query() declared methods of interface User!!
		while(true){
			System.out.printf("Welcome %s%nCustomer Menu%n", this.Name);
			System.out.printf("1) Search item%n2) Checkout cart%n3) Reward won%n4) Print latest orders%n5) Exit%n");
			int query=Integer.valueOf(scan.next());
			if(query==1){
				// Searching
				System.out.println("Choose a category");
				_Categories.display_Categories();
				if(_Categories.All_categories.size()==0){
					System.out.println("No category!");
					continue;
				}
				int category_Option=Integer.valueOf(scan.next());
				_Categories.display_all_Items(category_Option);
				System.out.println("Enter item code");
				int item_Option=Integer.valueOf(scan.next());
				Item temp_Item=null; // Bought Item
				for(int i=0;i<_Categories.All_categories.get(category_Option-1).size();i++){
					if(item_Option==_Categories.All_categories.get(category_Option-1).get(i).get_Code()){
						temp_Item=_Categories.All_categories.get(category_Option-1).get(i);
						break;
					}
				}
				System.out.println("Enter item quantity");
				int Quantity=Integer.valueOf(scan.next());
				System.out.printf("Choose method of transaction%n1) But item%n2) Add item to cart%n3) Exit%n");
				int transaction_Option=Integer.valueOf(scan.next());
				if(transaction_Option==1){
					// Buy item
					boolean valid_Purchase=this.validate_Purchase(temp_Item, Quantity);
					if(valid_Purchase){
						this.post_Purchase(temp_Item, Quantity, Company_Balance);
						this.Orders++; // Increment Orders
						ArrayList<Items> new_Order=new ArrayList<Items>();
						Items new_Items=new Items(temp_Item, Quantity);
						new_Order.add(new_Items);
						this.All_orders.add(new_Order); // Add to Recent Orders
						System.out.println("Item Successfully bought");
						this.get_Reward(); // Update Rewards 
					}
				}
				else if(transaction_Option==2){
					// Adds to cart
					boolean valid_Purchase=this.validate_Purchase(temp_Item, Quantity);
					if(valid_Purchase){
						this.add_Cart(temp_Item, Quantity);
					}
				}
				else{
					// Exit option
				}
			}
			else if(query==2){
				// Checkout cart
				ArrayList<Items> new_Items=new ArrayList<Items>();
				boolean is_removed=false;
				for(int i=0;i<this.Cart.size();i++){
					Items temp_Items=this.Cart.get(i);
					boolean valid_Purchase=this.validate_Purchase(temp_Items.get_Item(),temp_Items.get_Quantity());
					if(valid_Purchase){
						// If purchase if valid
						this.post_Purchase(temp_Items.get_Item(), temp_Items.get_Quantity(), Company_Balance);
						new_Items.add(temp_Items);
						System.out.println("Item Successfully bought");
					}
					else{
						// Removes purchased items from the cart
						for(int j=i-1;j>-1;j--){
							this.Cart.remove(j);
						}
						is_removed=true;
						break;
					}
				}
				if(!is_removed){
					while(this.Cart.size()>0){
						this.Cart.remove(0);
					}
				}
				if(new_Items.size()!=0){
					// If a purchase has been made, add it to the Recent orders of the Customer and check for Reward
					this.Orders++;
					this.All_orders.add(new_Items);
					this.get_Reward();
				}
			}
			else if(query==3){
				// Prints Rewards won
				this.display_Reward();
			}
			else if(query==4){
				// Prints recent orders in required format 
				this.recent_Orders();
			}
			else{
				// Exit option
				return;
			}
		}
	}	
}
interface People{
	/*
		Implemented by Merchants, Customers and Users Class
	*/
	public void choose_Person(Scanner scan, Categories _Categories, Account Company_Balance);
	// These methods only for correct initialisation of Users Class  
	public Merchant[] return_Merchants();
	public Customer[] return_Customers();
}
class Merchants implements People{
	private final Merchant[] all_Merchants=new Merchant[6]; // Array of pre-defined 5 Merchants
	public Merchants(){
		this.all_Merchants[1]=new Merchant("jack", "Toronto");
		this.all_Merchants[2]=new Merchant("john", "New York");
		this.all_Merchants[3]=new Merchant("james", "London");
		this.all_Merchants[4]=new Merchant("jeff", "Singapore");
		this.all_Merchants[5]=new Merchant("joseph", "Mumbai");
	}
	@Override
	public Merchant[] return_Merchants(){
		// Overrides the interface methods!!
		return this.all_Merchants;
	}
	@Override
	public Customer[] return_Customers(){
		// Overrides the interface methods!!
		// Just to satisfy required return type
		Customer[] foo=new Customer[1];
		return foo;	
	}
	@Override
	public void choose_Person(Scanner scan, Categories _Categories, Account Company_Balance){
		/*
			Overrides the interface methods!!
			Displays all Merchants 
		*/
		System.out.println("choose merchant");
		this.display_Merchants();
		int Id_option=Integer.valueOf(scan.next());
		User temp_Merchant=this.all_Merchants[Id_option];
		User_Menu _User_Menu=new User_Menu();
		_User_Menu.select_Menu(scan, temp_Merchant, _Categories, Company_Balance);
	}
	public void display_Merchants(){
		for(int i=1;i<6;i++){
			System.out.printf("%d %s%n", i, this.all_Merchants[i].get_Name());
		}
	}
}
class Customers implements People{
	private final Customer[] all_Customers=new Customer[6]; // Array of pre-defined 5 Customers
	public Customers(){
		this.all_Customers[1]=new Customer("ali", "Milwaukee");
		this.all_Customers[2]=new Customer("nobby", "Dalas");
		this.all_Customers[3]=new Customer("bruno", "Bejing");
		this.all_Customers[4]=new Customer("borat", "Paris");
		this.all_Customers[5]=new Customer("aladeen", "Berlin");
	}
	@Override
	public Merchant[] return_Merchants(){
		// Overrides the interface methods!!
		// Just to satisfy required return type
		Merchant[] foo=new Merchant[1];
		return foo;	
	}
	@Override
	public Customer[] return_Customers(){
		// Overrides the interface methods!!
		return this.all_Customers;
	}
	@Override
	public void choose_Person(Scanner scan, Categories _Categories, Account Company_Balance){
		/*
			Overrides the interface methods!!
			Displays all Customers
		*/
		System.out.println("choose customer");
		this.display_Customers();
		int Id_option=Integer.valueOf(scan.next());
		User temp_Customer=this.all_Customers[Id_option];
		User_Menu _User_Menu=new User_Menu();
		_User_Menu.select_Menu(scan, temp_Customer, _Categories, Company_Balance);
	}
	public void display_Customers(){
		for(int i=1;i<6;i++){
			System.out.printf("%d %s%n", i, this.all_Customers[i].get_Name());
		}
	}
}
class Users implements People{
	private final Merchant[] all_Merchants;
	private final Customer[] all_Customers;
	public Users(Merchant[] all_Merchants, Customer[] all_Customers){
		this.all_Merchants=all_Merchants;
		this.all_Customers=all_Customers;
	}
	@Override
	public Merchant[] return_Merchants(){
		// Overrides the interface methods!!
		// Just to satisfy required return type
		Merchant[] foo=new Merchant[1];
		return foo;	
	}
	@Override
	public Customer[] return_Customers(){
		// Overrides the interface methods!!
		// Just to satisfy required return type
		Customer[] foo=new Customer[1];
		return foo;	
	}
	@Override
	public void choose_Person(Scanner scan, Categories _Categories, Account Company_Balance){
		/*
			Overrides the interface methods!!
			Displays all Users (Merchants & Customers)
		*/
		System.out.println("choose user");
		this.display_Users();
		String type=scan.next();
		int Id_option=Integer.valueOf(scan.next());
		if(type.equals("M")){
			Merchant temp_Merchant=this.all_Merchants[Id_option];
			temp_Merchant.display_Details();
		}
		else{
			Customer temp_Customer=this.all_Customers[Id_option];
			temp_Customer.display_Details();
		}
	}
	public void display_Users(){
		System.out.println("M");
		for(int i=1;i<this.all_Merchants.length;i++){
			System.out.printf("%d %s%n", i, this.all_Merchants[i].get_Name());
		}
		System.out.println("C");
		for(int i=1;i<this.all_Customers.length;i++){
			System.out.printf("%d %s%n", i, this.all_Customers[i].get_Name());
		}
	}
}
class Main_Menu{ 
	private final People new_Merchants;
	private final People new_Customers;
	private final People new_Users;
	private final Account Company_Balance;
	private Categories new_Categories;
	public Main_Menu(){
		this.new_Merchants=new Merchants();
		this.new_Customers=new Customers();
		Merchant[] temp_Merchants=new_Merchants.return_Merchants();
		Customer[] temp_Customers=new_Customers.return_Customers();
		this.new_Users=new Users(temp_Merchants, temp_Customers);
		this.new_Categories=new Categories();
		this.Company_Balance=new Account();
	}
	public void Main_Menu_Options(Scanner scan){
		/*
			Initial Main Menu
		*/
		while(true){
			System.out.println("Welcome to Mercury");
			System.out.printf("1) Enter as Merchant%n2) Enter as Customer%n3) See user details%n4) Company account balance%n5) Exit%n");
			int query=Integer.valueOf(scan.next());
			if(query==1){
				// Enter as Merchant
				this.select_Person(scan, new_Merchants, new_Categories, Company_Balance);
			}
			else if(query==2){
				// Enter as Customer
				this.select_Person(scan, new_Customers, new_Categories, Company_Balance);
			}
			else if(query==3){
				// See user details
				this.select_Person(scan, new_Users, new_Categories, Company_Balance);
			}
			else if(query==4){
				// Company Account Balance
				System.out.println(this.Company_Balance.Balance);
			}
			else{
				return; // Exit
			}
		}
	}
	public void select_Person(Scanner scan, People _People, Categories _Categories, Account Company_Balance){
		/*
			Polymorphism choose_Person according to parameter passed as _People!!
		*/
		_People.choose_Person(scan, _Categories, Company_Balance);
	}
}
public class Mercury_Inc{
	public static void main(String[] args) {
		Scanner scan=new Scanner(System.in);
		Main_Menu _Main_Menu=new Main_Menu();
		_Main_Menu.Main_Menu_Options(scan);
		// Invokes the Main Menu and rest taken care subsequently 
	}
}