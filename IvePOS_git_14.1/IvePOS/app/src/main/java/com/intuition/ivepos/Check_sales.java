//<?php
//        $servername = "13.232.180.189";
//        $username = "irfan";
//        $password = "Intuition123";
//
//// Create connection
//        $conn = new mysqli($servername, $username, $password);
//// Check connection
//        if ($conn->connect_error) {
//        die("Connection failed: " . $conn->connect_error);
//        }
//
//        set_time_limit(500);
//        if( isset($_POST["company"]) && isset($_POST["store"] )&& isset($_POST["device"] ))
//        {
//
//        $company=$_POST["company"];
//        $store=$_POST["store"];
//        $device=$_POST["device"];
//
//// Create database
//        $sql = "CREATE DATABASE if not exists ".$company."_".$store."_".$device."_mydbsalesdata";
//        if ($conn->query($sql) === TRUE) {
//
//
//        $sql2 = array( "CREATE TABLE if not exists All_Sales (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, itemname text, quantity text, price text, total text,
//        type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, table_id text, billtype text,
//        paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, deleted_not text, modifiedquantity text, quantitycopy text,
//        modifiedtotal text, date1 text, datetimee text, disc_type text, disc_value text , newtotal text, disc_thereornot text,
//        disc_indiv_total text, new_modified_total text)",
//
//        "CREATE TABLE if not exists Itemwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, itemno text, itemname text, sales integer, salespercentage integer,
//        itemtotalquan text)",
//        "CREATE TABLE if not exists Itemwiseorderlistmodifiers (_id integer PRIMARY KEY UNIQUE, modno text, modname text, sales integer, salespercentage integer,
//        modtotalquan text)",
//        "CREATE TABLE if not exists Userwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, username text, sales integer, salespercentage integer)",
//        "CREATE TABLE if not exists Generalorderlistascdesc (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text,
//        billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text)",
//        "CREATE TABLE if not exists Generalorderlistascdesc1 (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text,
//        billdetails text, sales integer, discountamount text, paymentmethod text, billtype text, itemname text, quan text, tableid text, individualprice text
//        , date1 text, datetimee text)",
//        "CREATE TABLE if not exists userdata (_id integer PRIMARY KEY UNIQUE, username text, total integer)",
//        "CREATE TABLE if not exists itemdata (_id integer PRIMARY KEY UNIQUE, itemname text, total integer)",
//
//        "CREATE TABLE if not exists All_Sales_Cancelled (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, itemname text, quantity text, price text, total text,
//        type text, parent text, parentid text, mod_assigned text, tax text, taxname text, bill_no text, time text, date text, user text, billtype text,
//        paymentmethod text, billamount_disapply text, billamount_disnotapply text, _idd text, reason text,
//        billamount_cancelled text, date1 text, billamount_cancelled_user text, disc_type text, disc_value text, newtotal text, disc_thereornot text,
//        disc_indiv_total text)",
//
//        "CREATE TABLE if not exists Cancelwiseorderlistitems (_id integer PRIMARY KEY UNIQUE, date text, time text, user text, billno text, sale text,
//        refund text, reason text )",
//
//        "CREATE TABLE if not exists usercancelleddata (_id integer PRIMARY KEY UNIQUE, username text, total integer)",
//        "CREATE TABLE if not exists Customerdetails (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, name text, phoneno text, emailid text, address text,
//        rupees text, billnumber text)",
//        "CREATE TABLE if not exists Tablepayment (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, tablename text, tableid text, price text, print text)",
//        "CREATE TABLE if not exists Billnumber (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, billnumber text, total text, user text, date text,
//        paymentmethod text, billtype text, subtotal text, taxtotal text, roundoff text, globaltaxtotal text)",
//        "CREATE TABLE if not exists Discountdetails (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, date text, time text, billno text, Discountcodeno text,
//        Discount_percent text, Billamount_rupess text, Discount_rupees text, date1 text, original_amount text)",
//        "CREATE TABLE if not exists Cardnumber (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, cardnumber text)",
//        "CREATE TABLE if not exists Splitdata (_id integer PRIMARY KEY UNIQUE, billnum text, total text, splittype text, split1 text, split2 text, split3 text)",
//        "CREATE TABLE if not exists Cust_feedback (_id integer PRIMARY KEY UNIQUE, cust_name text, date text, time text, ambience_rating text, pro_qual_rating text,
//        service_rating text, overall_exp_rating text, comments text, percentage text, cust_phoneno text)",
//        "CREATE TABLE if not exists Clicked_cust_name (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, name text)",
//        "CREATE TABLE if not exists Customerdetails_temporary (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, commission_charges text, commission_charges_status text,
//        commission_charges_type text, phoneno text, name text)",
//        "CREATE TABLE if not exists Cusotmer_activity_temp (_id integer PRIMARY KEY UNIQUE, name text, phone_no text,
//        email text, addr text, total_amount text, balance text, discount_value text, discount_type text, approval_rate text)",
//        "CREATE TABLE if not exists Cusotmer_activity_temp_top3 (_id integer PRIMARY KEY UNIQUE, name text, phone_no text,
//        email text, addr text, total_amount integer, balance text, discount_value text, discount_type text, approval_rate text)",
//        "CREATE TABLE if not exists BillCount (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, value text)",
//        "CREATE TABLE if not exists Write_off (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, date text, time text, name text, phoneno text, write_off text)");
//
//
//        for ($i = 1; $i <= 100; $i++) {
//
//        $q1= "CREATE TABLE if not exists Table" . $i ." (_id integer PRIMARY KEY AUTO_INCREMENT,quantity text, itemname text, price text, total text, type text," .
//        " parent text, parentid text, modassigned text, tax text, taxname text, disc_type text, disc_value text, newtotal text, disc_thereornot text," .
//        " disc_indiv_total text)";
//
//        $q2="CREATE TABLE if not exists Table" . $i . "payment (_id integer PRIMARY KEY AUTO_INCREMENT, tableid text, price text, type text, paymentmethod text, " .
//        " discount text, discounttype text, discountcodenum text, cust_name text, cust_phone_no text, cust_emailid text, cust_address text, due_amount text," .
//        " cardnumber text, amounttendered text, dialog_round text, hometotal text, disc_type text, disc_value text, newtotal text, disc_thereornot text," .
//        " disc_indiv_total text)";
//
//        $q3="CREATE TABLE if not exists Table" . $i . "management (_id integer PRIMARY KEY AUTO_INCREMENT, itemname text, qty text, tagg integer, date text, ".
//        " time text, par_id text, itemtype text)";
//        array_push($sql2,$q1,$q2,$q3);
//        }
//
//        $q1="CREATE TABLE if not exists Top_Reason (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, reason text, value integer)";
//        $q2="CREATE TABLE if not exists Top_Category (_id integer PRIMARY KEY AUTO_INCREMENT UNIQUE, category text, value integer)";
//        $q3="CREATE TABLE if not exists Itemwiseorderlistcategory (_id integer PRIMARY KEY UNIQUE, itemno text, categoryname text, sales integer,
//        salespercentage integer,itemtotalquan text)";
//        array_push($sql2,$q1,$q2,$q3);
//
//        $dbname = $company."_".$store."_".$device."_mydbsalesdata";
//
//// Create connection
//        $conn1 = new mysqli($servername, $username, $password, $dbname);
//// Check connection
//        if ($conn1->connect_error) {
//        die("Connection failed: " . $conn->connect_error);
//        echo "error1";
//        }
//
//
//        $result=TRUE;
//        for ($x = 0; $x < sizeof($sql2); $x++) {
//
//        if ($conn1->query($sql2[$x]) === TRUE) {
//
//        } else {
//        echo "error".$x."\n".$conn1->error;
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        }
//
//        if($result==TRUE){
//
//
//
//        $sql3="SELECT * FROM Cardnumber";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==2){
//        $sql4="ALTER TABLE Cardnumber ADD COLUMN billnumber text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Generalorderlistascdesc1";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==16){
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN cardnumber text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==27){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN disc_type text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN disc_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN newtotal text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN disc_thereornot text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN disc_indiv_total text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN new_modified_total text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Table1";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==11){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table".$j." ADD COLUMN disc_type text ";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Table1";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==16){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table" . $j . " ADD COLUMN status text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Table1";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==17){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table" . $j ." ADD COLUMN tagg integer";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//        $sql3="SELECT * FROM Table1";
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==18){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table" . $j . " ADD COLUMN date text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Table" . $j . " ADD COLUMN time text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Table1";
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==20){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table".$j." ADD COLUMN updated_quantity text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Table1";
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==21){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN taxname2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Table".$j." ADD COLUMN tax2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Table".$j." ADD COLUMN taxname3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Table".$j. " ADD COLUMN tax3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Table" .$j." ADD COLUMN taxname4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Table" .$j." ADD COLUMN tax4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Table" .$j." ADD COLUMN taxname5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN tax5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//        $sql3="SELECT * FROM Table1";
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==29){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN category text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Table1";
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==30){
//
//        for($j=1;$j<=100;$j++){
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN add_note text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN dept_name text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN discount_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN discount_code text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN discount_type text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Table" .$j. " ADD COLUMN barcode_get text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//        $sql3="SELECT * FROM Billnumber";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==11){
//        $sql4="ALTER TABLE Billnumber ADD COLUMN billcount text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Generalorderlistascdesc1";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==17){
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN individualtotal text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN billcount text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Generalorderlistascdesc1";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==19){
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN hsn_code text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_per text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN globaltax_rs text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Generalorderlistascdesc1";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==24){
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_name5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Generalorderlistascdesc1 ADD COLUMN itemtax_rs5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Discountdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==10){
//        $sql4="ALTER TABLE Discountdetails ADD COLUMN billcount text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Cancelwiseorderlistitems";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==8){
//        $sql4="ALTER TABLE Cancelwiseorderlistitems ADD COLUMN billcount text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==7){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN date1 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN time1 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN date text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN total text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN deposit text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN cashout text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN credit text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN charges text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN authentication_pin text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN otp text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN dob text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==18){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN refunds text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN total_amount text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==20){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN cashout_type text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==21){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN credit_default text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==22){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN commission_charges text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN commission_charges_type text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN commission_charges_status text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==25){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN authentication_pin_status text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==26){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN dob_alaram text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==27){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN default_discount_status text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN default_discount_amount text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN default_discount_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN default_discount_type text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==31){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN notes text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==32){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN cust_account_no text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN cust_ifsc_code text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN cust_account_holder_name text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN cust_bank_name text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==33){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN datetimee_new text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==34){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN hsn_code text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==35){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN taxname2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN tax2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN taxname3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN tax3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN taxname4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN tax4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN taxname5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN tax5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==43){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN category text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN counterperson_username text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN counterperson_name text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==46){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN credit text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales ADD COLUMN Phone_num text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==48){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN barcode_get text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM All_Sales";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==49){
//        $sql4="ALTER TABLE All_Sales ADD COLUMN order_id text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM All_Sales_Cancelled";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==24){
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_type text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN newtotal text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_thereornot text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN disc_indiv_total text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM All_Sales_Cancelled";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==29){
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN datetimee_new text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM All_Sales_Cancelled";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==30){
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN tax2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN tax3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN tax4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN taxname5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN tax5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE All_Sales_Cancelled ADD COLUMN hsn_code text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//        $sql3="SELECT * FROM Billnumber";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==12){
//        $sql4="ALTER TABLE Billnumber ADD COLUMN datetimee_new text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==36){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN datetimee_new text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Discountdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==11){
//        $sql4="ALTER TABLE Discountdetails ADD COLUMN datetimee_new text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Discountdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==12){
//        $sql4="ALTER TABLE Discountdetails ADD COLUMN paymentmethod text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Cardnumber";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==3){
//        $sql4="ALTER TABLE Cardnumber ADD COLUMN datetimee_new text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==37){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN user_id text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==38){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax1 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax2 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax3 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax4 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax5 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax6 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax7 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax8 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax9 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax10 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax11 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax12 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax13 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax14 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax15 text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax_selection text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax1_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax2_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax3_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax4_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax5_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax6_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax7_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax8_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax9_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax10_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax11_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax12_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax13_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax14_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN tax15_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Billnumber";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==13){
//        $sql4="ALTER TABLE Billnumber ADD COLUMN comments_sales text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Billnumber";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==14){
//        $sql4="ALTER TABLE Billnumber ADD COLUMN delivery_fee text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Billnumber ADD COLUMN packing_charges text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Cusotmer_activity_tempr";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==11){
//        $sql4="ALTER TABLE Cusotmer_activity_temp ADD COLUMN cust_id text ";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==69){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN proceedings text ";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==70){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN SaleType text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN Cheque_num text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN CreditAmount text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN SaleTime text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN SaleDate text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==75){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN Transaction_ID text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN Card_Type text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN Card_Num text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN RRN text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==79){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN pincode text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==80){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN limit_status text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN limit_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN limit_present_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        $sql3="SELECT * FROM Customerdetails";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==83){
//        $sql4="ALTER TABLE Customerdetails ADD COLUMN write_off text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//
//
//
//        $sql3="SELECT * FROM Itemwiseorderlistitems";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==6){
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN category text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//
//        $sql3="SELECT * FROM Itemwiseorderlistitems";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==7){
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_buying_price text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_buying_price text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN i_cost_percent text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN mi_cost_percent text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Itemwiseorderlistitems";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==13){
//        $sql4="ALTER TABLE Itemwiseorderlistitems ADD COLUMN barcode_value text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//
//
//        $sql3="SELECT * FROM Itemwiseorderlistmodifiers";
//
//        if ($results=mysqli_query($conn1,$sql3))
//        {
//        // Return the number of fields in result set
//        $fieldcount=mysqli_num_fields($results);
//        if($fieldcount==6){
//        $sql4="ALTER TABLE Itemwiseorderlistmodifiers ADD COLUMN category text";
//        if ($conn1->query($sql4) === TRUE) {
//
//        }else{
//        $result=FALSE; echo $sql4."\n".$conn1->error;
//        }
//
//
//        }
//        // Free result set
//        mysqli_free_result($results);
//        }
//
//
//        if($result==TRUE){
//        echo "success";
//        }else{
//        echo "error11";
//        }
//        }else{
//        echo "error22";
//        }
//        } else {
//        echo "error33" ;
//        }
//        }
//
//        $conn->close();
//        ?>