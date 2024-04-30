package com.intuition.ivepos.signup;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import androidx.annotation.Nullable;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;


public class DatabaseService extends IntentService {


    private SQLiteDatabase db,db1;

    public DatabaseService() {
        super("DatabaseService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("splashhh6");
        createDatabase();


    }

    private void createDatabase() {

        System.out.println("splashhh7");

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);


//        final Thread t1=new Thread(new Runnable() {
//            @Override
//            public void run() {

                try {
                    db.execSQL("CREATE TABLE if not exists Adminrights (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists BIllingmode (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype text);");
                    db.execSQL("CREATE TABLE if not exists Barcodescannerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, barcodescannercontype text);");
                    db.execSQL("CREATE TABLE if not exists CATEGORY (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ALL_CATEGORY text);");
                    db.execSQL("CREATE TABLE if not exists Cashdrawerconnectivity (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cashdrawercontype text);");
                    db.execSQL("CREATE TABLE if not exists Companydetailss (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, companyname text, doorno text," +
                            "substreet text, street text, city text, state text, country text, pincode INTEGER, phoneno INTEGER, taxone text, taxtwo text, footerone text, footertwo text," +
                            "address1 text, email text, website text, address2 text, address3 text );");
                    db.execSQL("CREATE TABLE if not exists Hotel (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, direccion text, telefono text, email text);");
                    db.execSQL("CREATE TABLE if not exists Items (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                            "image blob, weekdaysvalue text, weekendsvalue text, manualstockvalue text, automaticstockresetvalue text, clickcount text, favourites text," +
                            "disc_type text, disc_value text, image_text text, barcode_value text, checked text, print_value text);");
                    db.execSQL("CREATE TABLE if not exists Items1 (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, price NUMERIC, stockquan NUMERIC, category text, itemtax text," +
                            "image blob, weekdaysvalue TEXT, weekendsvalue TEXT, manualstockvalue TEXT, automaticstockresetvalue TEXT, clickcount TEXT, favourites TEXT);");
                    db.execSQL("CREATE TABLE if not exists Itemsstockvalue (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, weekdaysvalue text, weekendsvalue text);");
                    db.execSQL("CREATE TABLE if not exists LAdmin (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");
                    db.execSQL("CREATE TABLE if not exists LOGIN (ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, USERNAME text, PASSWORD textpublic, name text);");
                    db.execSQL("CREATE TABLE if not exists Logo (_id INTEGER PRIMARY KEY UNIQUE, companylogo blob);");
                    db.execSQL("CREATE TABLE if not exists Modifiers (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, modifiername text, modprice numeric, modstockquan numeric, " +
                            "modcategory text, moditemtax text, modimage BLOB, mod_image_text text);");
                    db.execSQL("CREATE TABLE if not exists Printerconnectivity (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, printercontype text);");
                    db.execSQL("CREATE TABLE if not exists Printerreceiptsize(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, papersize text)");
                    db.execSQL("CREATE TABLE if not exists Quickaccess (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text);");
                    db.execSQL("CREATE TABLE if not exists Quickedit (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, quickedittype text);");
                    db.execSQL("CREATE TABLE if not exists ResetFrequencyRestaurant (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyrestaurant text);");
                    db.execSQL("CREATE TABLE if not exists ResetFrequencyRetail (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, resetfrequencyretail text);");
                    db.execSQL("CREATE TABLE if not exists Stockcontrol (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockcontroltype text);");
                    db.execSQL("CREATE TABLE if not exists Itemsort (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, itemsorttype text);");
                    db.execSQL("CREATE TABLE if not exists Stockreset (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresettype text);");
                    db.execSQL("CREATE TABLE if not exists Stockresetmode (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, stockresetoptionsmode text);");
                    db.execSQL("CREATE TABLE if not exists Storedays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, weekdays text, weekends text, swap text);");
                    db.execSQL("CREATE TABLE if not exists Universalcredentials (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists User1 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists User2 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists User3 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists User4 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists User5 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists User6 (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, name text, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists Taxes (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, taxname text, value numeric, taxtype text);");
                    db.execSQL("CREATE TABLE if not exists Discount_types (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, discountname text, discountvalue numeric);");
                    db.execSQL("CREATE TABLE if not exists Totaltables (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nooftables text);");
                    db.execSQL("CREATE TABLE if not exists asd1 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

                    db.execSQL("CREATE TABLE if not exists LoginUser (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text);");
                    db.execSQL("CREATE TABLE if not exists UserLogin_Checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, username text, password text, name text);");


                    db.execSQL("CREATE TABLE if not exists Alaramon_off (_id integer PRIMARY KEY UNIQUE, status text);");
                    db.execSQL("CREATE TABLE if not exists Alaramdays (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, selecteddays TEXT, unselecteddays text, swap TEXT);");
                    db.execSQL("CREATE TABLE if not exists Alaramtime (_id integer PRIMARY KEY UNIQUE, time text);");

                    db.execSQL("CREATE TABLE if not exists BTConn (_id integer PRIMARY KEY UNIQUE, name text, address text, status text, device text);");
                    db.execSQL("CREATE TABLE if not exists IPConn (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

                    db.execSQL("CREATE TABLE if not exists Menulogin_checking (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");
                    db.execSQL("CREATE TABLE if not exists Home_check (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, login_status text);");
                    //dbllega.execSQL("CREATE TABLE if not exists asd2 (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, pName text, pDate text, image blob);");

                    db.execSQL("CREATE TABLE if not exists DeleteDBon_off (_id integer PRIMARY KEY UNIQUE, status text);");
                    db.execSQL("CREATE TABLE if not exists Auto_generate_barcode (_id integer PRIMARY KEY UNIQUE, generate text);");
                    db.execSQL("CREATE TABLE if not exists DeleteDB_time (_id integer PRIMARY KEY UNIQUE, time text);");
                    db.execSQL("CREATE TABLE if not exists Email_setup (_id integer PRIMARY KEY UNIQUE, username text, password text, client text);");
                    db.execSQL("CREATE TABLE if not exists Default_credit (_id integer PRIMARY KEY UNIQUE, status text);");
                    db.execSQL("CREATE TABLE if not exists Working_hours (_id integer PRIMARY KEY UNIQUE, opening text, opening_time text, closing text, closing_time text," +
                            "opening_time_system text, closing_time_system text);");
                    db.execSQL("CREATE TABLE if not exists Printer_text_size (_id integer PRIMARY KEY UNIQUE, type text);");
                    db.execSQL("CREATE TABLE if not exists Change_time_format (_id integer PRIMARY KEY UNIQUE, status text);");
                    db.execSQL("CREATE TABLE if not exists Hotel1 (_id INTEGER PRIMARY KEY UNIQUE, name text, value int);");
                    db.execSQL("CREATE TABLE if not exists Discount_details (_id INTEGER PRIMARY KEY UNIQUE, disc_code text, disc_value text, disc_type text);");
                    db.execSQL("CREATE TABLE if not exists Email_recipient (_id integer PRIMARY KEY UNIQUE, name text, ph_no text, email text);");

                    db.execSQL("CREATE TABLE if not exists Schedule_mail_on_off (_id integer PRIMARY KEY UNIQUE, status text);");
                    db.execSQL("CREATE TABLE if not exists Schedule_mail_time (_id integer PRIMARY KEY UNIQUE, time text);");
                    db.execSQL("CREATE TABLE if not exists promotions (_id integer PRIMARY KEY UNIQUE, email text);");

                    db.execSQL("CREATE TABLE if not exists User_privilege (_id integer PRIMARY KEY UNIQUE, username text, returns_refunds text, product_tax text, reports text," +
                            "settings text, backup text, customer text);");

                    db.execSQL("CREATE TABLE if not exists Tax_selec (_id integer PRIMARY KEY UNIQUE, tax_amount text, tax_per text, selected_but text);");
                    db.execSQL("CREATE TABLE if not exists Discount_selec (_id integer PRIMARY KEY UNIQUE, discount_amount text, discount_per text, selected_but text);");
                    db.execSQL("CREATE TABLE if not exists Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                            "vendor_email text, vendor_address text, vendor_gst);");
                    db.execSQL("CREATE TABLE if not exists Vendor_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                            "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                            "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
                    db.execSQL("CREATE TABLE if not exists Vendor_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                            "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                            "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                            "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                            "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                            "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text);");
                    db.execSQL("CREATE TABLE if not exists Ingredient_items_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, ingredient_name text, itemname text, item_qyt_used text," +
                            "currnet_stock text, date1 text, date text, time1 text, time text, modified_datetimee_new text, qty_unit text, required text);");
                    db.execSQL("CREATE TABLE if not exists Vendor_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                            "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
                    db.execSQL("CREATE TABLE if not exists Items_temp_list (_id integer PRIMARY KEY UNIQUE, itemname text, avg_price text, min_price text," +
                            "max_price text, total_qty text, total_price text, barcode text, not_required text);");

                    db.execSQL("CREATE TABLE if not exists Ingredient_Vendor_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                            "vendor_email text, vendor_address text, vendor_gst text);");
                    db.execSQL("CREATE TABLE if not exists Ingredient_sold_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                            "invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text, disc_amount text, total_bill_amount text, from_time text," +
                            "from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text, total_pay text, pay_date text, pay_time text, pay_datetimeemew text, not_required text);");
                    db.execSQL("CREATE TABLE if not exists Ingredients (_id integer PRIMARY KEY UNIQUE, ingredient_name text, min_req text, max_req text, current_stock text," +
                            "unit text, indiv_price text, total_price text, date text, date1 text, time text, time1 text, datetimee_new text, avg_price text, required text, barcode text," +
                            "status_low text, status_qty_updated text, add_qty text, indiv_price_copy text, adjusted_stock text, diff_stock text, indiv_price_temp text," +
                            "total_price_temp text);");
                    db.execSQL("CREATE TABLE if not exists Ingredient_sold_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text," +
                            "itemname text, qty_add text, individual_price text, total_price text, invoice text, billamount text, tax_percent text, tax_amount text, disc_percent text," +
                            "disc_amount text, total_bill_amount text, from_time text, from_date text, due_date text, datetimee_new_from text, datetimee_new_due text, pay text, pending text," +
                            "tax1 text, tax2 text, tax3 text, tax4 text, tax5 text, tax6 text, tax7 text, tax8 text, tax9 text, tax10 text, tax11 text, tax12 text, tax13 text," +
                            "tax14 text, tax15 text, tax1_value text, tax2_value text, tax3_value text, tax4_value text, tax5_value text, tax6_value text, tax7_value text," +
                            "tax8_value text, tax9_value text, tax10_value text, tax11_value text, tax12_value text, tax13_value text, tax14_value text, tax15_value text," +
                            "wastage text, unit text);");
                    db.execSQL("CREATE TABLE if not exists Ingredients_item_selection_temp (_id integer PRIMARY KEY UNIQUE, itemname text, qty_temp text, qty_temp_unit text, qty text);");
                    db.execSQL("CREATE TABLE if not exists Vendor_details_micro (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vendor_phoneno text, " +
                            "vendor_email text, vendor_address text, vendor_gst text);");
                    db.execSQL("CREATE TABLE if not exists Vendor_temp_list_Ingredient (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vendor_name text, vend_phon text, vend_email text," +
                            "vend_gst text, vend_address text, vend_total_bill_amount text, paid text, pending text, bill_no text);");
                    db.execSQL("CREATE TABLE if not exists Ingredients_temp_list (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, avg_price text, min_price text," +
                            "max_price text, total_qty text, total_price text, barcode text, unit text, wastage_qty text, wastage_cost text, not_required text);");
                    db.execSQL("CREATE TABLE if not exists Printer_type (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, printer_type text);");

                    db.execSQL("CREATE TABLE if not exists KOT_print (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, kot_print_status text);");
                    db.execSQL("CREATE TABLE if not exists Auto_Connect (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, auto_connect_status text);");
                    db.execSQL("CREATE TABLE if not exists Weighing_Scale_status (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, Weighing_Scale_onoff text);");
                    db.execSQL("CREATE TABLE if not exists Weighing_Scale_name (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, scale_name text);");
                    db.execSQL("CREATE TABLE if not exists Sync_time (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, last_time text);");
                    db.execSQL("CREATE TABLE if not exists variants_temp (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, vari1 text, varprice1 text, vari2 text, varprice2 text," +
                            "vari3 text, varprice3 text, vari4 text, varprice4 text, vari5 text, varprice5 text);");

//Payment Integration Tables
                    //Payment Tables Paytm, Mobikwik, mSwipe
                    db.execSQL("CREATE TABLE if not exists PaytmMerchantReg(_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, Account text, MerchantName text, " +
                            "guid text, MID text, Merchant_key text, PosID text)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS MobikwikMerchantReg(_id integer primary key autoincrement unique, Account text, Merchant_name text, " +
                            "Mid_otp text, Secretkey_otp text, Mid_debit text, Secretkey_debit text)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS all_transactions" +
                            "(_id integer primary key autoincrement unique, Payment_medium text, merchantRefInvoiceNo text, amount text, cardHolderName text," +
                            " cardBrand text, cardType text, cardNumber text, paymentId text, transactionId text, tdrPercentage text, approved text)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS CardSwiperActivation" +
                            "(_id integer primary key autoincrement unique, CardSwiperName text, merchantKey text, partnerkey text, Config_status text)");

                    db.execSQL("CREATE TABLE if not exists IPConn_Counter (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
                    db.execSQL("CREATE TABLE if not exists Country_Selection (_id integer PRIMARY KEY UNIQUE, country text);");

                    db.execSQL("CREATE TABLE if not exists Round_off (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, round_off_status text);");


                    db.execSQL("CREATE VIRTUAL TABLE if not exists Items_Virtual USING fts3(itemname , price , stockquan , category , itemtax ," +
                            "image , weekdaysvalue , weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites ," +
                            "disc_type , disc_value , image_text , barcode_value , checked , print_value , quantity_sold , minimum_qty , minimum_qty_copy , add_qty ," +
                            "status_low , status_qty_updated , individual_price , unit_type , tax_value , itemtax2 , tax_value2 , itemtax3 ,tax_value3 ," +
                            "itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , status_temp , variant1 , variant_price1 , variant2 , variant_price2 , variant3 ," +
                            "variant_price3 , variant4 , variant_price4 , variant5 , variant_price5)");

                    db.execSQL("CREATE TABLE if not exists BIllingtype (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billingtype_type text);");
                    db.execSQL("CREATE TABLE if not exists Estimate_print (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");

                    db.execSQL("CREATE TABLE if not exists IPConn_KOT1 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
                    db.execSQL("CREATE TABLE if not exists IPConn_KOT2 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
                    db.execSQL("CREATE TABLE if not exists IPConn_KOT3 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");
                    db.execSQL("CREATE TABLE if not exists IPConn_KOT4 (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");


                    db.execSQL("CREATE TABLE if not exists Name_Dept1 (_id integer PRIMARY KEY UNIQUE, dept1_name text);");
                    db.execSQL("CREATE TABLE if not exists Name_Dept2 (_id integer PRIMARY KEY UNIQUE, dept2_name text);");
                    db.execSQL("CREATE TABLE if not exists Name_Dept3 (_id integer PRIMARY KEY UNIQUE, dept3_name text);");
                    db.execSQL("CREATE TABLE if not exists Name_Dept4 (_id integer PRIMARY KEY UNIQUE, dept4_name text);");

                    db.execSQL("CREATE TABLE if not exists Ordertaking_server_ip (_id integer PRIMARY KEY UNIQUE, ipname text, port text, status text);");

                    db.execSQL("CREATE TABLE if not exists HomeDelivery_prints (_id integer PRIMARY KEY UNIQUE, companycopy text, customercopy text);");
                    db.execSQL("CREATE TABLE if not exists Billcount_tag (_id integer PRIMARY KEY UNIQUE, tag_name text);");

                    db.execSQL("CREATE TABLE if not exists Stock_transfer_item_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, qty_add text," +
                            "company text, from_store text, from_device text, to_store text, to_device text, from_time text, from_date text, datetimee_new_from text);");

                    db.execSQL("CREATE TABLE if not exists Restaurant_id (_id integer PRIMARY KEY UNIQUE, merchant_id text);");


                    db.execSQL("CREATE TABLE if not exists Online_order_form (_id integer PRIMARY KEY UNIQUE, date_of_order text, rest_name text, rest_addre text, rest_num text," +
                            "rest_emailid text, gst_no text, owners_name text, owners_num text, owners_email_id text, zomato_rela_manag_email_id text, zomato_regi_email_id text," +
                            "zomato_order_url text, menu_timings text, deli_charg text, pack_charg text, gst_any text, contact_num text, swiggy_relat_manag_email_id text," +
                            "swiggy_order_url text);");

                    //------------To create table for wera online orders
                    /*db.execSQL("CREATE TABLE if not exists Online_order_wera (_id integer PRIMARY KEY UNIQUE, order_type text, order_date_time text, payment_mode text, restaurant_id text," +
                            "restaurant_name text, external_order_id text,restaurant_address text, restaurant_number text, order_from text, enable_delivery text, net_amount text, gross_amount text, " +
                            "order_instructions text, gst text, cgst text, sgst text,  packaging text, order_packaging text, packaging_cgst_percent text, packaging_sgst_percent text, packaging_cgst text, packaging_sgst text,  " +
                            " order_packaging_cgst text, order_packaging_sgst text, order_packaging_cgst_percent text, order_packaging_sgst_percent text, delivery_charge text, discount_reason text," +
                            "discount text, cus_name text, cus_phone_number text, cus_email text, cus_address text, cus_delivery_area text, cus_address_instructions text," +
                            "num_item text);");*/


                    db.execSQL("CREATE TABLE if not exists loyalty_points (_id integer PRIMARY KEY UNIQUE, money text, point text, point2 text, money2 text);");
                    db.execSQL("CREATE TABLE if not exists loyalty_points_status (_id integer PRIMARY KEY UNIQUE, status text);");

                    db.execSQL("CREATE TABLE if not exists Modifiers_new (_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, modifierset text, modifiername text, modprice text," +
                            "modstockquan text, items text);");

                    db.execSQL("CREATE TABLE if not exists Account_selection (_id integer PRIMARY KEY UNIQUE, selection text);");

                    db.execSQL("CREATE TABLE if not exists Version_Control (_id integer PRIMARY KEY UNIQUE, version text);");

                    db.execSQL("CREATE TABLE if not exists Freetrail (_id integer PRIMARY KEY UNIQUE, status text);");

                    db.execSQL("CREATE TABLE if not exists Floors (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, floorname text, position text);");

                    db.execSQL("CREATE TABLE IF NOT EXISTS RazorpayMerchantReg (_id integer primary key autoincrement unique, account text, account_id text)");

                    db.execSQL("CREATE TABLE if not exists Table_kot (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, status text);");

                    db.execSQL("CREATE TABLE IF NOT EXISTS EzetapMerchantReg (_id integer primary key autoincrement unique, account text, userid text, password text)");

//                    db.execSQL("ALTER TABLE Ingredients MODIFY COLUMN _id int AUTOINCREMENT");

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co4 = db.rawQuery("SELECT * FROM Modifiers", null);
                    int cou4 = co4.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou4).equals("7")){
                        db.execSQL("ALTER TABLE Modifiers ADD COLUMN mod_image_text text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co4.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co4_1 = db.rawQuery("SELECT * FROM Hotel", null);
                    int cou4_1 = co4_1.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou4_1).equals("5")){
                        db.execSQL("ALTER TABLE Hotel ADD COLUMN value int");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co4_1.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co1 = db.rawQuery("SELECT * FROM Items", null);
                    int cou1 = co1.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou1).equals("13")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN disc_type text DEFAULT 0");
                        db.execSQL("ALTER TABLE Items ADD COLUMN disc_value text DEFAULT 0");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co1.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co3 = db.rawQuery("SELECT * FROM Items", null);
                    int cou3 = co3.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou3).equals("15")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN image_text text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co3.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co5 = db.rawQuery("SELECT * FROM Items", null);
                    int cou5 = co5.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou5).equals("16")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN barcode_value text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co5.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co6 = db.rawQuery("SELECT * FROM Items", null);
                    int cou6 = co6.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou6).equals("17")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN checked text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co6.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co7 = db.rawQuery("SELECT * FROM Items", null);
                    int cou7 = co7.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou7).equals("18")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN print_value text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co7.close();


                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co87 = db.rawQuery("SELECT * FROM User_privilege", null);
                    int cou87 = co87.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou87).equals("8")){
                        db.execSQL("ALTER TABLE User_privilege ADD COLUMN ingredients text DEFAULT no");
                        db.execSQL("ALTER TABLE User_privilege ADD COLUMN subscriptions text DEFAULT no");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co87.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co88 = db.rawQuery("SELECT * FROM Items", null);
                    int cou88 = co88.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou88).equals("19")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN quantity_sold INTEGER DEFAULT 0");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co88.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co881 = db.rawQuery("SELECT * FROM Items", null);
                    int cou881 = co881.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou881).equals("20")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty text DEFAULT 0");
                        db.execSQL("ALTER TABLE Items ADD COLUMN minimum_qty_copy text DEFAULT 0");
                        db.execSQL("ALTER TABLE Items ADD COLUMN add_qty text DEFAULT 0");
                        db.execSQL("ALTER TABLE Items ADD COLUMN status_low text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN individual_price text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co881.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co882 = db.rawQuery("SELECT * FROM Items", null);
                    int cou882 = co882.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou882).equals("26")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN unit_type text DEFAULT Unit");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co882.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co883 = db.rawQuery("SELECT * FROM Items", null);
                    int cou883 = co883.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou883).equals("27")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN tax_value text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN itemtax2 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN tax_value2 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN itemtax3 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN tax_value3 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN itemtax4 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN tax_value4 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN itemtax5 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN tax_value5 text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co883.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co884 = db.rawQuery("SELECT * FROM Items", null);
                    int cou884 = co884.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou884).equals("36")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN status_temp text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN status_perm text");
                    }
                    co884.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co885 = db.rawQuery("SELECT * FROM Items", null);
                    int cou885 = co885.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou885).equals("38")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant1 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant_price1 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant2 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant_price2 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant3 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant_price3 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant4 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant_price4 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant5 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN variant_price5 text");
                    }
                    co885.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co886 = db.rawQuery("SELECT * FROM Items", null);
                    int cou886 = co886.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou886).equals("48")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN dept_name text");
                    }
                    co886.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co887 = db.rawQuery("SELECT * FROM Items", null);
                    int cou887 = co887.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou887).equals("49")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN add_qty_st text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN status_qty_updated_st text");
                    }
                    co887.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co888 = db.rawQuery("SELECT * FROM Items", null);
                    int cou888 = co888.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou888).equals("51")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN out_of_stock text");
                    }
                    co888.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co889 = db.rawQuery("SELECT * FROM Items", null);
                    int cou889 = co889.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou889).equals("52")) {
                        db.execSQL("ALTER TABLE Items ADD COLUMN mod_set1 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN mod_set2 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN mod_set3 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN mod_set4 text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN mod_set5 text");
                    }
                    co889.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co100 = db.rawQuery("SELECT * FROM Items", null);
                    int cou100 = co100.getColumnCount();
                    if (String.valueOf(cou100).equals("57")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN is_veg text");
                    }
                    co100.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co101 = db.rawQuery("SELECT * FROM Items", null);
                    int cou101 = co101.getColumnCount();
                    if (String.valueOf(cou101).equals("58")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN packing_charges text");
                    }
                    co101.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co102 = db.rawQuery("SELECT * FROM Items", null);
                    int cou102 = co102.getColumnCount();
                    if (String.valueOf(cou102).equals("59")){
                        db.execSQL("ALTER TABLE Items ADD COLUMN v1_oofstock text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN v2_oofstock text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN v3_oofstock text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN v4_oofstock text");
                        db.execSQL("ALTER TABLE Items ADD COLUMN v5_oofstock text");
                    }
                    co102.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co89 = db.rawQuery("SELECT * FROM Taxes", null);
                    int cou89 = co89.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou89).equals("4")){
                        db.execSQL("ALTER TABLE Taxes ADD COLUMN checked text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co89.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co90 = db.rawQuery("SELECT * FROM Taxes", null);
                    int cou90 = co90.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou90).equals("5")){
                        db.execSQL("ALTER TABLE Taxes ADD COLUMN tax1 DEFAULT dine_in");
                        db.execSQL("ALTER TABLE Taxes ADD COLUMN tax2 DEFAULT takeaway");
                        db.execSQL("ALTER TABLE Taxes ADD COLUMN tax3 DEFAULT homedelivery");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co90.close();


                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co901 = db.rawQuery("SELECT * FROM Taxes", null);
                    int cou901 = co901.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou901).equals("8")){
                        db.execSQL("ALTER TABLE Taxes ADD COLUMN hsn_code text");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co901.close();


                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co91 = db.rawQuery("SELECT * FROM IPConn", null);
                    int cou91 = co91.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou91).equals("4")){
                        db.execSQL("ALTER TABLE IPConn ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co91.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co92 = db.rawQuery("SELECT * FROM IPConn_Counter", null);
                    int cou92 = co92.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou92).equals("4")){
                        db.execSQL("ALTER TABLE IPConn_Counter ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co92.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co93 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                    int cou93 = co93.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou93).equals("4")){
                        db.execSQL("ALTER TABLE IPConn_KOT1 ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co93.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co931 = db.rawQuery("SELECT * FROM IPConn_KOT1", null);
                    int cou931 = co931.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou931).equals("5")){
                        db.execSQL("ALTER TABLE IPConn_KOT1 ADD COLUMN kot_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co93.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co94 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                    int cou94 = co94.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou94).equals("4")){
                        db.execSQL("ALTER TABLE IPConn_KOT2 ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co94.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co941 = db.rawQuery("SELECT * FROM IPConn_KOT2", null);
                    int cou941 = co941.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou941).equals("5")){
                        db.execSQL("ALTER TABLE IPConn_KOT2 ADD COLUMN kot_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co94.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co95 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                    int cou95 = co95.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou95).equals("4")){
                        db.execSQL("ALTER TABLE IPConn_KOT3 ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co95.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co951 = db.rawQuery("SELECT * FROM IPConn_KOT3", null);
                    int cou951 = co951.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou951).equals("5")){
                        db.execSQL("ALTER TABLE IPConn_KOT3 ADD COLUMN kot_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co95.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co96 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                    int cou96 = co96.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou96).equals("4")){
                        db.execSQL("ALTER TABLE IPConn_KOT4 ADD COLUMN printer_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co96.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co961 = db.rawQuery("SELECT * FROM IPConn_KOT4", null);
                    int cou961 = co961.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou961).equals("5")){
                        db.execSQL("ALTER TABLE IPConn_KOT4 ADD COLUMN kot_name text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co961.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co97 = db.rawQuery("SELECT * FROM asd1", null);
                    int cou97 = co97.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou97).equals("4")){
                        db.execSQL("ALTER TABLE asd1 ADD COLUMN floor text DEFAULT first");
                        db.execSQL("ALTER TABLE asd1 ADD COLUMN position text DEFAULT 0");
                        db.execSQL("ALTER TABLE asd1 ADD COLUMN max text DEFAULT 4");
                        db.execSQL("ALTER TABLE asd1 ADD COLUMN present text");
//                Toast.makeText(MainActivity.this, "altered", Toast.LENGTH_SHORT).show();
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co97.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co98 = db.rawQuery("SELECT * FROM EzetapMerchantReg", null);
                    int cou98 = co98.getColumnCount();
                    if (String.valueOf(cou98).equals("4")){
                        db.execSQL("ALTER TABLE EzetapMerchantReg ADD COLUMN prodAppKey text");
                        db.execSQL("ALTER TABLE EzetapMerchantReg ADD COLUMN orgCode text");
                    }
                    co98.close();

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
                    Cursor co99 = db.rawQuery("SELECT * FROM CardSwiperActivation", null);
                    int cou99 = co99.getColumnCount();
                    if (String.valueOf(cou99).equals("5")){
                        db.execSQL("ALTER TABLE CardSwiperActivation ADD COLUMN prodAppKey text");
                        db.execSQL("ALTER TABLE CardSwiperActivation ADD COLUMN orgCode text");
                    }
                    co99.close();

                    Cursor cursor2 = db.rawQuery("SELECT * FROM Items_Virtual", null);
                    if (cursor2.moveToFirst()) {


                    } else {
                        db.execSQL("INSERT INTO Items_Virtual (itemname , price , stockquan , category , itemtax , image" +
                                " , weekdaysvalue , weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites ,disc_type" +
                                " , disc_value , image_text , barcode_value , checked , print_value, quantity_sold , minimum_qty , minimum_qty_copy ,add_qty" +
                                ", status_low , status_qty_updated , individual_price , unit_type , tax_value , itemtax2 , tax_value2 , itemtax3 ,tax_value3" +
                                ", itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , variant1 , variant_price1 , variant2 , variant_price2 , variant3" +
                                ", variant_price3 , variant4 , variant_price4 , variant5 , variant_price5 ) SELECT itemname, price , stockquan , category , itemtax,image , weekdaysvalue " +
                                ", weekendsvalue , manualstockvalue , automaticstockresetvalue , clickcount , favourites , disc_type , disc_value , image_text , barcode_value , checked" +
                                " , print_value , quantity_sold , minimum_qty , minimum_qty_copy ,add_qty , status_low , status_qty_updated , individual_price , unit_type , tax_value " +
                                ", itemtax2 , tax_value2 , itemtax3 ,tax_value3 , itemtax4 ,tax_value4 ,itemtax5 ,tax_value5 , status_perm , variant1 , variant_price1 , variant2 , variant_price2 , variant3" +
                                ", variant_price3 , variant4 , variant_price4 , variant5 , variant_price5  FROM Items");

                    }
                    cursor2.close();




//            Intent intenta = new Intent(SplashScreenActivity.this, DrawerService.class);
//            startService(intenta);
//            Intent intentb = new Intent(SplashScreenActivity.this, DrawerService1.class);
//            startService(intentb);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch(Exception e){
                    e.printStackTrace();
                }


//            }
//        },"t1");

        db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

//        final Thread t2=new Thread(new Runnable() {
//            @Override
//            public void run() {

                try {
                    db1.execSQL("CREATE TABLE if not exists All_Sales (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                            "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, table_id text, billtype text," +
                            "paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, deleted_not text, modifiedquantity text, quantitycopy text, " +
                            "modifiedtotal text, date1 text, datetimee text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                            " disc_indiv_total text, new_modified_total text);");

                    db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, itemno text, itemname text, sales integer, salespercentage integer," +
                            "itemtotalquan text);");
                    db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistmodifiers (_id integer PRIMARY KEY UNIQUE, modno text, modname text, sales integer, salespercentage integer," +
                            "modtotalquan text);");
                    db1.execSQL("CREATE TABLE if not exists Userwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, username text, sales integer, salespercentage integer);");
                    db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                            "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text);");
                    db1.execSQL("CREATE TABLE if not exists Generalorderlistascdesc1 (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, " +
                            "billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text, tableid text, individualprice text" +
                            ", date1 text, datetimee text);");
                    db1.execSQL("CREATE TABLE if not exists userdata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
                    db1.execSQL("CREATE TABLE if not exists itemdata (_id integer PRIMARY KEY UNIQUE, itemname text, total integer);");

                    db1.execSQL("CREATE TABLE if not exists All_Sales_Cancelled (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, quantity text, price text, total text," +
                            "type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, billtype text," +
                            " paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, reason text, " +
                            "billamount_cancelled text, date1 text, billamount_cancelled_user text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                            " disc_indiv_total text);");

                    db1.execSQL("CREATE TABLE if not exists Cancelwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, sale text, " +
                            "refund text, reason text );");

                    db1.execSQL("CREATE TABLE if not exists usercancelleddata (_id integer PRIMARY KEY UNIQUE, username text, total integer);");
                    db1.execSQL("CREATE TABLE if not exists Customerdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text, phoneno text, emailid text, address text, " +
                            "rupees text, billnumber text);");
                    db1.execSQL("CREATE TABLE if not exists Tablepayment (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, tablename text, tableid text, price text, print text);");
                    db1.execSQL("CREATE TABLE if not exists Billnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billnumber text, total text, user text, date text," +
                            " paymentmethod text, billtype text, subtotal text, taxtotal text, roundoff text, globaltaxtotal text);");
                    db1.execSQL("CREATE TABLE if not exists Discountdetails (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, date text, time text, billno text, Discountcodeno text, " +
                            "Discount_percent text, Billamount_rupess text, Discount_rupees text, date1 text, original_amount text);");
                    db1.execSQL("CREATE TABLE if not exists Cardnumber (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, cardnumber text);");
                    db1.execSQL("CREATE TABLE if not exists Splitdata (_id integer PRIMARY KEY UNIQUE, billnum text, total text, splittype text, split1 text, split2 text, split3 text);");
                    db1.execSQL("CREATE TABLE if not exists Cust_feedback (_id integer PRIMARY KEY UNIQUE, cust_name text, date text, time text, ambience_rating text, pro_qual_rating text," +
                            " service_rating text, overall_exp_rating text, comments text, percentage text, cust_phoneno text);");
                    db1.execSQL("CREATE TABLE if not exists Clicked_cust_name (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, name text);");
                    db1.execSQL("CREATE TABLE if not exists Customerdetails_temporary (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, commission_charges text, commission_charges_status text, " +
                            "commission_charges_type text, phoneno text, name text);");
                    db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                            "email text, addr text, total_amount text, balance text, discount_value text, discount_type text, approval_rate text);");
                    db1.execSQL("CREATE TABLE if not exists Cusotmer_activity_temp_top3 (_id integer PRIMARY KEY UNIQUE, name text, phone_no text, " +
                            "email text, addr text, total_amount integer, balance text, discount_value, text, discount_type text, approval_rate text);");

                    for (int i=1;i<=100;i++ ){
                        db1.execSQL("CREATE TABLE if not exists Table" + i + " (_id integer PRIMARY KEY AUTOINCREMENT,quantity text, itemname text, price text, total text, type text," +
                                " parent text, parentid text, modassigned text, tax text, taxname text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                                " disc_indiv_total text);");

                        db1.execSQL("CREATE TABLE if not exists Table" + i + "payment (_id integer PRIMARY KEY AUTOINCREMENT, tableid text, price text, type text, paymentmethod text, " +
                                " discount text, discounttype text, discountcodenum text, cust_name text, cust_phone_no text, cust_emailid text, cust_address text, due_amount text," +
                                " cardnumber text, amounttendered text, dialog_round text, hometotal text, disc_type text, disc_value text, newtotal text, disc_thereornot text," +
                                " disc_indiv_total text);");

                        db1.execSQL("CREATE TABLE if not exists Table" + i + "management (_id integer PRIMARY KEY AUTOINCREMENT, itemname text, qty text, tagg integer, date text, " +
                                " time text, par_id text, itemtype text);");
                    }

                    db1.execSQL("CREATE TABLE if not exists Top_Reason (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, reason text, value integer);");
                    db1.execSQL("CREATE TABLE if not exists Top_Category (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, category text, value integer);");
                    db1.execSQL("CREATE TABLE if not exists Itemwiseorderlistcategory (_id integer PRIMARY KEY UNIQUE, itemno text, categoryname text, sales integer, salespercentage integer," +
                            "itemtotalquan text);");

                    db1.execSQL("CREATE TABLE if not exists BillCount (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, value text);");

                    db1.execSQL("CREATE TABLE if not exists Write_off (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, date text, time text, name text, phoneno text, write_off text);");


                    db1.execSQL("CREATE TABLE if not exists customer_loyalty_points (_id integer PRIMARY KEY UNIQUE, phoneno text, points text);");

                    db1.execSQL("CREATE TABLE if not exists Expenses_sales (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, expensename text, price text, category text, counterperson_username text, counterperson_name text, date text, time text, datetimee_new text);");

                    db1.execSQL("CREATE TABLE if not exists Kotcancelled (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, itemname text, qty text, tableid text, reason text, date text, date1 text, time text, datetimee_new text, category text," +
                            "user text, kot_no text, itemtype text, price text, total text);");

                    db1.execSQL("CREATE TABLE if not exists Razorpay_trans_details (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, billnumber text, account_id text, event text, id text, order_id text, description text, rrn text, upi_transaction_id text);");

                    //------------To create table for wera online orders
                    db1.execSQL("CREATE TABLE if not exists online_order_wera (_id integer PRIMARY KEY UNIQUE, order_type text, order_date_time text, payment_mode text, restaurant_id text," +
                            "restaurant_name text, external_order_id text,restaurant_address text, restaurant_number text, order_from text, enable_delivery text, net_amount text, gross_amount text," +
                            "order_instructions text, gst text, cgst text, sgst text,  packaging text, order_packaging text, packaging_cgst_percent text, packaging_sgst_percent text, packaging_cgst text, packaging_sgst text," +
                            " order_packaging_cgst text, order_packaging_sgst text, order_packaging_cgst_percent text, order_packaging_sgst_percent text, delivery_charge text, discount_reason text," +
                            "discount text, cus_name text, cus_phone_number text, cus_email text, cus_address text, cus_delivery_area text, cus_address_instructions text," +
                            "num_item text, order_status text, order_reason text, rider_name text, rider_number text, rider_status text);");

                    db1.execSQL("CREATE TABLE if not exists online_wera_store (_id integer PRIMARY KEY UNIQUE, open_status text, menu_upload_ref text);");

                    //db1.execSQL("CREATE TABLE if not exists wera_index_table (_id integer PRIMARY KEY AUTOINCREMENT UNIQUE, order_status text, companyname text, storename text, devicename text, restaurant_id text);");

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co2 = db1.rawQuery("SELECT * FROM Cardnumber", null);
                    int cou2 = co2.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou1), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou2).equals("2")){
                        db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN billnumber text DEFAULT 0");
                        //db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
                    }
                    co2.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
                    int cou = co.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou).equals("16")){
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN cardnumber text DEFAULT 0");
                    }
                    co.close();

//                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//                    Cursor co5 = db1.rawQuery("SELECT * FROM All_Sales", null);
//                    int cou5 = co5.getColumnCount();
//                    if (String.valueOf(cou5).equals("27")){
//                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_type text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_value text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN newtotal text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_thereornot text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN disc_indiv_total text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN new_modified_total text DEFAULT 0");
//                    }
//                    co5.close();

//                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//                    Cursor co6 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
//                    int cou6 = co6.getColumnCount();
//                    if (String.valueOf(cou6).equals("24")){
//                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_type text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_value text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN newtotal text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_thereornot text DEFAULT 0");
//                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_indiv_total text DEFAULT 0");
//                    }
//                    co6.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co7 = db1.rawQuery("SELECT * FROM Table1", null);
                    int cou7 = co7.getColumnCount();
                    if (String.valueOf(cou7).equals("11")){
                        for (int i=1;i<=100;i++ ){
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_type text DEFAULT 0");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_value text DEFAULT 0");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN newtotal text DEFAULT 0");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_thereornot text DEFAULT 0");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN disc_indiv_total text DEFAULT 0");
                        }
                    }
                    co7.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo7 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou7 = coo7.getColumnCount();
                    if (String.valueOf(coou7).equals("16")){
                        for (int i=1;i<=100;i++ ){
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN status text");
                        }
                    }
                    coo7.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo8 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou8 = coo8.getColumnCount();
                    if (String.valueOf(coou8).equals("17")){
                        for (int i=1;i<=100;i++ ){
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tagg integer");
                        }
                    }
                    coo8.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo9 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou9 = coo9.getColumnCount();
                    if (String.valueOf(coou9).equals("18")){
                        for (int i=1;i<=100;i++ ){
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN date text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN time text");
                        }
                    }
                    coo9.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo10 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou10 = coo10.getColumnCount();
                    if (String.valueOf(coou10).equals("20")){
                        for (int i=1;i<=100;i++ ){
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN updated_quantity text");
                        }
                    }
                    coo10.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo11 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou11 = coo11.getColumnCount();
                    if (String.valueOf(coou11).equals("21")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname2 text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax2 text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname3 text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax3 text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname4 text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax4 text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN taxname5 text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN tax5 text");
                        }
                    }
                    coo11.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo12 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou12 = coo12.getColumnCount();
                    if (String.valueOf(coou12).equals("29")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN category text");
                        }
                    }
                    coo12.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo13 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou13 = coo13.getColumnCount();
                    if (String.valueOf(coou13).equals("30")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN add_note text");
                        }
                    }
                    coo13.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo14 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou14 = coo14.getColumnCount();
                    if (String.valueOf(coou14).equals("31")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN dept_name text");
                        }
                    }
                    coo14.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo15 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou15 = coo15.getColumnCount();
                    if (String.valueOf(coou15).equals("32")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_value text");
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_code text");
                        }
                    }
                    coo15.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo16 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou16 = coo16.getColumnCount();
                    if (String.valueOf(coou16).equals("34")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN discount_type text");
                        }
                    }
                    coo16.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo17 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou17 = coo17.getColumnCount();
                    if (String.valueOf(coou17).equals("35")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN barcode_get text");
                        }
                    }
                    coo17.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo18 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou18 = coo18.getColumnCount();
                    if (String.valueOf(coou18).equals("36")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN old_total text");
                        }
                    }
                    coo18.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo19 = db1.rawQuery("SELECT * FROM Table1", null);
                    int coou19 = coo19.getColumnCount();
                    if (String.valueOf(coou19).equals("37")) {
                        for (int i = 1; i <= 100; i++) {
                            db1.execSQL("ALTER TABLE Table" + i + " ADD COLUMN old_total_indiv text");
                        }
                    }
                    coo19.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co71 = db1.rawQuery("SELECT * FROM Table1management", null);
                    int cou71 = co71.getColumnCount();
                    if (String.valueOf(cou71).equals("8")){
                        for (int i=1;i<=100;i++ ){
                            db1.execSQL("ALTER TABLE Table" + i + "management ADD COLUMN datetimee_new text");
                        }
                    }
                    co71.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co8 = db1.rawQuery("SELECT * FROM Billnumber", null);
                    int cou8 = co8.getColumnCount();
                    if (String.valueOf(cou8).equals("11")){
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN billcount text");
                    }
                    co8.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);

                    Cursor co9 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
                    int cou9 = co9.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou9).equals("17")){
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN individualtotal text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN billcount text");
                    }
                    co9.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo91 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
                    int coou91 = coo91.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(coou91).equals("19")){
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN hsn_code text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_per text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN globaltax_rs text");
                    }
                    coo91.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor coo911 = db1.rawQuery("SELECT * FROM Generalorderlistascdesc1", null);
                    int coou911 = coo911.getColumnCount();
                    if (String.valueOf(coou911).equals("24")) {
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name2 text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs2 text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name3 text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs3 text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name4 text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs4 text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name5 text");
                        db1.execSQL("ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs5 text");
                    }
                    coo911.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co10 = db1.rawQuery("SELECT * FROM Discountdetails", null);
                    int cou10 = co10.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou10).equals("10")){
                        db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN billcount text");
                    }
                    co10.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co11 = db1.rawQuery("SELECT * FROM Cancelwiseorderlistitems", null);
                    int cou11 = co11.getColumnCount();
                    //Toast.makeText(DatabaseService.this, " "+String.valueOf(cou), Toast.LENGTH_SHORT).show();
                    if (String.valueOf(cou11).equals("8")){
                        db1.execSQL("ALTER TABLE Cancelwiseorderlistitems ADD COLUMN billcount text");
                    }
                    co11.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co12 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou12 = co12.getColumnCount();
                    if (String.valueOf(cou12).equals("7")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date1 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN time1 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN date text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN deposit text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN charges text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN otp text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob text");
                    }
                    co12.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co13 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou13 = co13.getColumnCount();
                    if (String.valueOf(cou13).equals("18")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN refunds text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN total_amount text");
                    }
                    co13.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co14 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou14 = co14.getColumnCount();
                    if (String.valueOf(cou14).equals("20")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cashout_type text");
                    }
                    co14.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co15 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou15 = co15.getColumnCount();
                    if (String.valueOf(cou15).equals("21")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN credit_default text");
                    }
                    co15.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co16 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou16 = co16.getColumnCount();
                    if (String.valueOf(cou16).equals("22")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_type text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN commission_charges_status text");
                    }
                    co16.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co17 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou17 = co17.getColumnCount();
                    if (String.valueOf(cou17).equals("25")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN authentication_pin_status text");
                    }
                    co17.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co18 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou18 = co18.getColumnCount();
                    if (String.valueOf(cou18).equals("26")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN dob_alaram text");
                    }
                    co18.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co19 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou19 = co19.getColumnCount();
                    if (String.valueOf(cou19).equals("27")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_status text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_amount text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN default_discount_type text");
                    }
                    co19.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co20 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou20 = co20.getColumnCount();
                    if (String.valueOf(cou20).equals("31")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN notes text");
                    }
                    co20.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co21 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou21 = co21.getColumnCount();
                    if (String.valueOf(cou21).equals("32")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_no text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_ifsc_code text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_account_holder_name text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN cust_bank_name text");
                    }
                    co21.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co22 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou22 = co22.getColumnCount();
                    if (String.valueOf(cou22).equals("33")){
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN datetimee_new text");
                    }
                    co22.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co221 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou221 = co221.getColumnCount();
                    if (String.valueOf(cou221).equals("34")){
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code text");
                    }
                    co221.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co222 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou222 = co222.getColumnCount();
                    if (String.valueOf(cou222).equals("35")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname2 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax2 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname3 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax3 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname4 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax4 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN taxname5 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN tax5 text");
                    }
                    co222.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co223 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou223 = co223.getColumnCount();
                    if (String.valueOf(cou223).equals("43")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN category text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_username text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN counterperson_name text");
                    }
                    co223.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co224 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou224 = co224.getColumnCount();
                    if (String.valueOf(cou224).equals("46")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN credit text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN Phone_num text");
                    }
                    co224.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co225 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou225 = co225.getColumnCount();
                    if (String.valueOf(cou225).equals("48")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN barcode_get text");
                    }
                    co225.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co226 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou226 = co226.getColumnCount();
                    if (String.valueOf(cou226).equals("49")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN order_id text");
                    }
                    co226.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co227 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou227 = co227.getColumnCount();
                    if (String.valueOf(cou227).equals("50")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN old_total text");
                    }
                    co227.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co228 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou228 = co228.getColumnCount();
                    if (String.valueOf(cou228).equals("51")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code2 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code3 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code4 text");
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN hsn_code5 text");
                    }
                    co228.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co229 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou229 = co229.getColumnCount();
                    if (String.valueOf(cou229).equals("55")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN item_online text");
                    }
                    co229.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co230 = db1.rawQuery("SELECT * FROM All_Sales", null);
                    int cou230 = co230.getColumnCount();
                    if (String.valueOf(cou230).equals("56")) {
                        db1.execSQL("ALTER TABLE All_Sales ADD COLUMN old_total_indiv text");
                    }
                    co230.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co25 = db1.rawQuery("SELECT * FROM Billnumber", null);
                    int cou25 = co25.getColumnCount();
                    if (String.valueOf(cou25).equals("12")){
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN datetimee_new text");
                    }
                    co25.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co23 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
                    int cou23 = co23.getColumnCount();
                    if (String.valueOf(cou23).equals("29")){
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN datetimee_new text");
                    }
                    co23.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co231 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
                    int cou231 = co231.getColumnCount();
                    if (String.valueOf(cou231).equals("30")) {
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname2 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax2 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname3 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax3 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname4 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax4 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname5 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN tax5 text");
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN hsn_code text");
                    }
                    co231.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co232 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
                    int cou232 = co232.getColumnCount();
                    if (String.valueOf(cou232).equals("39")) {
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN old_total text");
                    }
                    co232.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co233 = db1.rawQuery("SELECT * FROM All_Sales_Cancelled", null);
                    int cou233 = co233.getColumnCount();
                    if (String.valueOf(cou233).equals("40")) {
                        db1.execSQL("ALTER TABLE All_Sales_Cancelled ADD COLUMN old_total_indiv text");
                    }
                    co233.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co24 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou24 = co24.getColumnCount();
                    if (String.valueOf(cou24).equals("36")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN datetimee_new text");
                    }
                    co24.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co26 = db1.rawQuery("SELECT * FROM Discountdetails", null);
                    int cou26 = co26.getColumnCount();
                    if (String.valueOf(cou26).equals("11")){
                        db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN datetimee_new text");
                    }
                    co26.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co261 = db1.rawQuery("SELECT * FROM Discountdetails", null);
                    int cou261 = co261.getColumnCount();
                    if (String.valueOf(cou261).equals("12")){
                        db1.execSQL("ALTER TABLE Discountdetails ADD COLUMN paymentmethod text");
                    }
                    co261.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co27 = db1.rawQuery("SELECT * FROM Cardnumber", null);
                    int cou27 = co27.getColumnCount();
                    if (String.valueOf(cou27).equals("3")){
                        db1.execSQL("ALTER TABLE Cardnumber ADD COLUMN datetimee_new text");
                    }
                    co27.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co241 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou241 = co241.getColumnCount();
                    if (String.valueOf(cou241).equals("37")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN user_id text");
                    }
                    co241.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co242 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou242 = co242.getColumnCount();
                    if (String.valueOf(cou242).equals("38")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15 text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax_selection text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax1_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax2_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax3_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax4_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax5_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax6_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax7_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax8_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax9_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax10_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax11_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax12_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax13_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax14_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN tax15_value text");



                    }
                    co242.close();


                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co251 = db1.rawQuery("SELECT * FROM Billnumber", null);
                    int cou251 = co251.getColumnCount();
                    if (String.valueOf(cou251).equals("13")){
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN comments_sales text");
                    }
                    co251.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co252 = db1.rawQuery("SELECT * FROM Billnumber", null);
                    int cou252 = co252.getColumnCount();
                    if (String.valueOf(cou252).equals("14")){
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN delivery_fee text");
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN packing_charges text");
                    }
                    co252.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co253 = db1.rawQuery("SELECT * FROM Billnumber", null);
                    int cou253 = co253.getColumnCount();
                    if (String.valueOf(cou253).equals("16")){
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN loyalty_points text");
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN loyalty_amount text");
                    }
                    co253.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co254 = db1.rawQuery("SELECT * FROM Billnumber", null);
                    int cou254 = co254.getColumnCount();
                    if (String.valueOf(cou254).equals("18")){
                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN order_id text");
                    }
                    co254.close();

//                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
//                    Cursor co254 = db1.rawQuery("SELECT * FROM Billnumber", null);
//                    int cou254 = co254.getColumnCount();
//                    if (String.valueOf(cou254).equals("18")){
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax1 text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax1_value text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax2 text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax2_value text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax3 text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax3_value text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax4 text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax4_value text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax5 text");
//                        db1.execSQL("ALTER TABLE Billnumber ADD COLUMN globaltax5_value text");
//                    }
//                    co254.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co28 = db1.rawQuery("SELECT * FROM Cusotmer_activity_temp", null);
                    int cou28 = co28.getColumnCount();
                    if (String.valueOf(cou28).equals("10")){
                        db1.execSQL("ALTER TABLE Cusotmer_activity_temp ADD COLUMN cust_id text DEFAULT off");
                        db1.execSQL("ALTER TABLE Cusotmer_activity_temp ADD COLUMN pincode text DEFAULT off");
                    }
                    co28.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co29 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou29 = co29.getColumnCount();
                    if (String.valueOf(cou29).equals("69")){
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN proceedings text DEFAULT off");
                    }
                    co29.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co290 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou290 = co290.getColumnCount();
                    if (String.valueOf(cou290).equals("70")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleType text DEFAULT off");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Cheque_num text DEFAULT off");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN CreditAmount text DEFAULT off");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleTime text DEFAULT off");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN SaleDate text DEFAULT off");
                    }
                    co290.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co291 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou291 = co291.getColumnCount();
                    if (String.valueOf(cou291).equals("75")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Transaction_ID text DEFAULT off");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Type text DEFAULT off");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN Card_Num text DEFAULT off");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN RRN text DEFAULT off");
                    }
                    co291.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co292 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou292 = co292.getColumnCount();
                    if (String.valueOf(cou292).equals("79")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN pincode text");
                    }
                    co292.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co293 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou293 = co293.getColumnCount();
                    if (String.valueOf(cou293).equals("80")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN limit_status text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN limit_value text");
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN limit_present_value text");
                    }
                    co293.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co294 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou294 = co294.getColumnCount();
                    if (String.valueOf(cou294).equals("83")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN write_off text");
                    }
                    co294.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co295 = db1.rawQuery("SELECT * FROM Customerdetails", null);
                    int cou295 = co295.getColumnCount();
                    if (String.valueOf(cou295).equals("84")) {
//                    SaleType text, Cheque_num text,CreditAmount text, SaleTime text, SaleDate text
                        db1.execSQL("ALTER TABLE Customerdetails ADD COLUMN loyal_points text");
                    }
                    co295.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co30 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
                    int cou30 = co30.getColumnCount();
                    if (String.valueOf(cou30).equals("6")){
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN category");
                    }
                    co30.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co301 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
                    int cou301 = co301.getColumnCount();
                    if (String.valueOf(cou301).equals("7")){
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_buying_price");
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_buying_price");
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_value");
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_percent");
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_value");
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_percent");
                    }
                    co301.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co302 = db1.rawQuery("SELECT * FROM Itemwiseorderlistitems", null);
                    int cou302 = co302.getColumnCount();
                    if (String.valueOf(cou302).equals("13")){
                        db1.execSQL("ALTER TABLE Itemwiseorderlistitems ADD COLUMN barcode_value");
                    }
                    co302.close();

                    db1 = openOrCreateDatabase("mydb_Salesdata", Context.MODE_PRIVATE, null);
                    Cursor co31 = db1.rawQuery("SELECT * FROM Itemwiseorderlistmodifiers", null);
                    int cou31 = co31.getColumnCount();
                    if (String.valueOf(cou31).equals("6")){
                        db1.execSQL("ALTER TABLE Itemwiseorderlistmodifiers ADD COLUMN category");
                    }
                    co31.close();






////////////////////Items image upload////////////////
                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

//        Cursor items1 = db.rawQuery("SELECT * FROM Items WHERE image is null", null);
//        if (items1.moveToFirst()){
//            do {
//                String id = items1.getString(0);
//                String name = items1.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (items1.moveToNext());
//        }
//
//        Cursor items2 = db.rawQuery("SELECT * FROM Items WHERE image = '' ", null);
//        if (items2.moveToFirst()){
//            do {
//                String id = items2.getString(0);
//                String name = items2.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (items2.moveToNext());
//        }

//        Cursor modifiers1 = db.rawQuery("SELECT * FROM Items WHERE image is null", null);
//        if (modifiers1.moveToFirst()){
//            do {
//                String id = modifiers1.getString(0);
//                String name = modifiers1.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (modifiers1.moveToNext());
//        }
//
//        Cursor modifiers2 = db.rawQuery("SELECT * FROM Items WHERE image = '' ", null);
//        if (modifiers2.moveToFirst()){
//            do {
//                String id = modifiers2.getString(0);
//                String name = modifiers2.getString(1);
//                //Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                String str1 = name.substring(0, 2);
//                String str2 = str1.toUpperCase();
//                ContentValues contentValues5 = new ContentValues();
//                contentValues5.put("image", "");
//                contentValues5.put("image_text", str2);
//                String where = "_id = '" + id + "'";
//                db.update("Items", contentValues5, where, new String[]{});
//            }while (modifiers2.moveToNext());
//        }



//        Cursor items = db.rawQuery("SELECT * FROM Items", null);
//        if (items.moveToFirst()){
//            do {
//                String id = items.getString(0);
//                String name = items.getString(1);
//                byte[] image = items.getBlob(6);
//                String cate = items.getString(4);
//                String tax = items.getString(5);
//
//                if (image.length < 0){
//                    Toast.makeText(MainActivity.this, "image not hter "+name, Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this, "image hter "+name, Toast.LENGTH_SHORT).show();
//                }
////                Bitmap bmPicture = BitmapFactory.decodeByteArray(image, 0, image.length);
////                if (bmPicture == null) {
////                    //Toast.makeText(MainActivity.this, "no image for "+id, Toast.LENGTH_SHORT).show();
////                    String str1 = name.substring(0, 2);
////                    String str2 = str1.toUpperCase();
////                    ContentValues contentValues5 = new ContentValues();
////                    contentValues5.put("image_text", str2);
////                    String where = "_id = '" + id + "'";
////                    db.update("Items", contentValues5, where, new String[]{});
////
////                }
////                if (image == null) {
////                    //Toast.makeText(MainActivity.this, "no image for "+id, Toast.LENGTH_SHORT).show();
////                    String str1 = name.substring(0, 2);
////                    String str2 = str1.toUpperCase();
////                    ContentValues contentValues5 = new ContentValues();
////                    contentValues5.put("image_text", str2);
////                    String where = "_id = '" + id + "'";
////                    db.update("Items", contentValues5, where, new String[]{});
////
////                }
//            }while (items.moveToNext());
//
//        }

                    db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);

                    Cursor fr2 = db.rawQuery("SELECT * FROM Items", null);
                    int numberOfRows2 = fr2.getCount();

                    int limit2 = 0;
                    if (numberOfRows2 > 100) {
                        while (limit2 + 100 < numberOfRows2) {
                            Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE favourites is null or favourites = '' LIMIT '"+ limit2+"', 100", null);
                            if (itemsc.moveToFirst()){
                                do {
                                    String id = itemsc.getString(0);
                                    ContentValues contentValues5 = new ContentValues();
                                    contentValues5.put("favourites", "no");
                                    String where = "_id = '" + id + "'";
                                    db.update("Items", contentValues5, where, new String[]{});

                                    String where1_v1 = "docid = '" + id + "' ";
                                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});

                                }while (itemsc.moveToNext());

                            }
                            itemsc.close();
                            limit2 += 100;
                        }
                        int news = numberOfRows2 - limit2;
                        if (news == 0){
                            //Toast.makeText(DatabaseService.this, "limit is b " + limit, Toast.LENGTH_SHORT).show();
                        }else {
                            Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE favourites is null or favourites = '' LIMIT '"+ news+"' OFFSET '"+limit2+"' ", null);
                            if (itemsc.moveToFirst()){
                                do {
                                    String id = itemsc.getString(0);
                                    ContentValues contentValues5 = new ContentValues();
                                    contentValues5.put("favourites", "no");
                                    String where = "_id = '" + id + "'";
                                    db.update("Items", contentValues5, where, new String[]{});

                                    String where1_v1 = "docid = '" + id + "' ";
                                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});

                                }while (itemsc.moveToNext());

                            }
                            itemsc.close();
                        }
                    }else {
                        Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE favourites is null or favourites = '' ", null);
                        if (itemsc.moveToFirst()){
                            do {
                                String id = itemsc.getString(0);
                                ContentValues contentValues5 = new ContentValues();
                                contentValues5.put("favourites", "no");
                                String where = "_id = '" + id + "'";
                                db.update("Items", contentValues5, where, new String[]{});

                                String where1_v1 = "docid = '" + id + "' ";
                                db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});

                            }while (itemsc.moveToNext());

                        }
                        itemsc.close();
                    }
                    fr2.close();
//////////////////////////////////
                    Cursor fr3 = db.rawQuery("SELECT * FROM Items", null);
                    int numberOfRows3 = fr3.getCount();

                    int limit3 = 0;
                    if (numberOfRows3 > 100) {
                        while (limit3 + 100 < numberOfRows3) {
                            Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE weekdaysvalue is null or weekdaysvalue = '' LIMIT '"+ limit3+"', 100", null);
                            if (itemsc.moveToFirst()){
                                do {
                                    String id = itemsc.getString(0);
                                    ContentValues contentValues5 = new ContentValues();
                                    contentValues5.put("weekdaysvalue", "0");
                                    String where = "_id = '" + id + "'";
                                    db.update("Items", contentValues5, where, new String[]{});

                                    String where1_v1 = "docid = '" + id + "' ";
                                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});

                                }while (itemsc.moveToNext());

                            }
                            itemsc.close();
                            limit3 += 100;
                        }
                        int news = numberOfRows3 - limit3;
                        if (news == 0){
                            //Toast.makeText(DatabaseService.this, "limit is b " + limit, Toast.LENGTH_SHORT).show();
                        }else {
                            Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE weekdaysvalue is null or weekdaysvalue = '' LIMIT '"+ news+"' OFFSET '"+limit3+"' ", null);
                            if (itemsc.moveToFirst()){
                                do {
                                    String id = itemsc.getString(0);
                                    ContentValues contentValues5 = new ContentValues();
                                    contentValues5.put("weekdaysvalue", "0");
                                    String where = "_id = '" + id + "'";
                                    db.update("Items", contentValues5, where, new String[]{});

                                    String where1_v1 = "docid = '" + id + "' ";
                                    db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});

                                }while (itemsc.moveToNext());

                            }
                            itemsc.close();
                        }
                    }else {
                        Cursor itemsc = db.rawQuery("SELECT * FROM Items WHERE weekdaysvalue is null or weekdaysvalue = '' ", null);
                        if (itemsc.moveToFirst()){
                            do {
                                String id = itemsc.getString(0);
                                ContentValues contentValues5 = new ContentValues();
                                contentValues5.put("weekdaysvalue", "0");
                                String where = "_id = '" + id + "'";
                                db.update("Items", contentValues5, where, new String[]{});

                                String where1_v1 = "docid = '" + id + "' ";
                                db.update("Items_Virtual", contentValues5, where1_v1, new String[]{});

                            }while (itemsc.moveToNext());

                        }
                        itemsc.close();
                    }
                    fr3.close();
////////////////////////////////////////








///////////////////////////modifiers default

//        Cursor modifiers = db.rawQuery("SELECT * FROM Modifiers", null);
//        if (modifiers.moveToFirst()){
//            do {
//                String id = modifiers.getString(0);
//                String name = modifiers.getString(1);
//                byte[] image = modifiers.getBlob(6);
//
//                if (image == null) {
//                    //Toast.makeText(MainActivity.this, "no image for "+id, Toast.LENGTH_SHORT).show();
//                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.item_bg_image2);
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    b.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                    img = bos.toByteArray();
//                    ContentValues contentValues5 = new ContentValues();
//                    contentValues5.put("modimage", img);
//                    String where = "_id = '" + id + "'";
//                    db.update("Modifiers", contentValues5, where, new String[]{});
//
//                }
//            }while (modifiers.moveToNext());
//
//        }


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch(Exception e){
                    e.printStackTrace();
                }

//            }
//        },"t2");


//        final Runnable r = new Runnable() {
//            public void run() {
//
//                t1.start();
//                t2.start();
//
//
//                try {
//                    t1.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    t2.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//                SharedPreferences prefs = getDefaultSharedPreferencesMultiProcess(getApplicationContext()); // 0 - for private mode
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean("signindb", true); // Storing boolean - true/false
//
//                editor.apply();
//
//            }
//        };
//        Handler handler=new Handler();
//        handler.postDelayed(r,10);






    }
}
