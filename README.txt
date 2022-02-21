***************
we will make a bag for every child
How to use the application
if you are user(parent)
1.create a account by clicking the button register in the main page 
2.fill the required fields with your deatils
3.fill your kids id in the son id field you can fill more than one id seperated with comma
4.now you can track the information about your sons by clicking the button search son and put your son Id on the textfield
5.you can also add kid on clicking the addkid button and putting your kid Id.
6.Enjoy!
instruction to set the esp:
to set the wifi:
1.open esp82.ino file
2. change the following feilds in line 7 where username is the name of the wifi
and password is the password of the wifi:
#define WIFI_SSID "username"
#define WIFI_PASSWORD "password"

***************
to set the id of the bag:
1.change 123456789 in line 108 to the kid's real id:
  String s = Firebase.getString("/kids/123456789/Score");
***************

click save and thats it
you're all set!