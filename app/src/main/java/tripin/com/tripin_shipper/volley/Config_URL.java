package tripin.com.tripin_shipper.volley;

//This class is for storing all URLs as a model of URLs 54.169.85.27/tripin/index.php/api/user/sign_up

public class Config_URL
{
	private static String base_URL = "http://54.169.85.27/tripin/api/";//"http://10.0.2.2:80/";		//Default configuration for WAMP - 80 is default port for WAMP and 10.0.2.2 is localhost IP in Android Emulator
	// Server user login url
	public static String URL_LOGIN = base_URL+"user/sign_in/";   ;//"android_login_api/";

	// Server user register url
	public static String URL_REGISTER = base_URL+"user/sign_up";   //"android_login_api/";

	// Server user otp url
	public static String URL_OTP = base_URL+"user/otp_validate";   //"android_login_api/";

	// Server user Resend otp url
	public static String URL_REOTP = base_URL+"user/resend_otp";   //"android_login_api/";

	// Server user Change Number
	public static String URL_CHANGE_NUMBER = base_URL+"user/sign_up_change_number"; //"Change number_api"

	// Forgot password
	public static String URL_FORGOT_PASSWORD = base_URL+"user/forgot_password";  //"Forgot PAssword"

	//Reset password
	public  static  String URL_RESET_PASSWORD = base_URL+"user/reset_password"; //"Reset Password"

	//Oauth
	public static String URL_OAUTH = "http://54.169.85.27/tripin/Oauth2/ClientCredentials/get_token";

	public static String Oauth_client_id = "testclient";
	public static String Oauth_grant_type = "client_credentials";
	public static String Oauth_client_secret = "testpass";

	//Satate
	public static String URL_STATE = "http://54.169.85.27/tripin/api/settings/load_states";
	//City
	public static String URL_CITY = "http://54.169.85.27/tripin/api/settings/load_cities";

	//Save Address
	public  static  String URL_SAVE_ADDRESS = base_URL+"settings/save_address"; //

	//User Address Book
	public static String URL_ADDRESS_BOOK = base_URL+"settings/load_address_book";

	// Truck Selection
	public  static String URL_TRUCK_SELECTION_PRICING = base_URL+"book/load_truck_selection";

	// Goods
	public  static String URL_GOODS = base_URL+"settings/load_goods";

	// chepest Route
	public  static String URL_CHEPEST_ROUTE = base_URL+"/settings/get_shortest_path";

	// get truck count
	public  static String URL_TRUCKCOUNT = base_URL+"book/load_avaiable_trucks_count";

	// Truck Selection NEW
	public  static String URL_TRUCK_SELECTION_PRICING_NEW = base_URL+"book/load_truck_selection_new";

	//Truck Allocation
	public  static String URL_TRUCK_ALLOCATION = base_URL+"book/truck_allocate";

	//Address delete
	public  static String URL_ADDRESS_DELETE = base_URL+"settings/delete_address";
}
