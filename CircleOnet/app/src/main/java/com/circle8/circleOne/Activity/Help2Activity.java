package com.circle8.circleOne.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.circle8.circleOne.Adapter.ExpandableListAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Help2Activity extends AppCompatActivity
{
    private ExpandableListView expListView;
    private List<String> questionsList;
    private List<String> answersList;
    private Map<String, List<String>> collectionList;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Utility.freeMemory();
        setContentView(R.layout.activity_help2);

        expListView = (ExpandableListView)findViewById(R.id.QA_listview);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        createGroupList();
        createCollection();

        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, questionsList, collectionList);
        expListView.setAdapter(expListAdapter);

        setGroupIndicatorToRight();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                finish();
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {
            // Keep track of previous expanded parent
            int previousGroup = -1;
            boolean flag = false;

            @Override
            public void onGroupExpand(int groupPosition)
            {
                Utility.freeMemory();
                // Collapse previous parent if expanded.
                if ((previousGroup != -1) && (groupPosition != previousGroup))
                {
                    expListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
                flag = true;
            }
        });
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }


    private void setGroupIndicatorToRight()
    {
        Utility.freeMemory();
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(30), width - getDipsFromPixel(3));
//        expListView.setGroupIndicator(getResources().getDrawable(R.drawable.ic_down_arrow));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.8f);
    }

    private void createGroupList()
    {
        Utility.freeMemory();
        questionsList = new ArrayList<String>();
        questionsList.add("My Account:");
        questionsList.add("Editing Your Profile:");
        questionsList.add("Changing Your Password:");
        questionsList.add("Scan QR Code:");
        questionsList.add("Lost or Stolen Cards:");
        questionsList.add("Request for a New Card:");
        questionsList.add("Damaged Cards:");
        questionsList.add("Request for a new Account:");
        questionsList.add("Requested Card/Cards did not arrive:");
        questionsList.add("Sync Contacts:");
        questionsList.add("Reward Points:");
        questionsList.add("Notifications:");
        questionsList.add("Circles:");
        questionsList.add("Subscription:");
        questionsList.add("History:");
        questionsList.add("Security:");
        questionsList.add("Sort and Filter:");
        questionsList.add("Profile:");
        questionsList.add("How do I edit my Profile?");
        questionsList.add("Who can access the information on my profile?");
        questionsList.add("Event:");
        questionsList.add("Connect:");
    }

    private void createCollection()
    {
        Utility.freeMemory();
        // preparing laptops collection(child)
        String[] firstAnswer =
                {
                         "Change Your Password",
                         "1) Under My Account, click 'Profile'",
                         "2) Click 'Change Password'" ,
                         "3) Enter new password",
                         "4) Click ‘Save’"
                };
        String[] secondAnswer =
                {
                        "If your CircleOne Card is lost or stolen, you should report it right away and we will cancel it with immediate effect",
                        "If you have more than 1 card, please follow these steps to identify your lost/stolen card:",
                        "1) Tap each card against your NFC-enabled mobile device and determine which profile is missing.",
                        "2) Select the “Report Lost/Stolen Card” menu from the “Contact Us” page on your App or web platform and tell us which profile is missing."
                };
        String[] thirdAnswer =
                {
                        "1) Log in to your CircleOne account.",
                        "2) Select tab named “Request for a new card”.",
                        "3) Follow the instructions regarding your artwork and details.",
                        "4) Check that your details are correct and proceed with your payment.",
                        "5) Once the order confirmation is received on our end, the card will be mailed within two - four weeks.",
                        "As of 25 September 2017: The card is currently free while stocks last."
                };
        String[] forthAnswer =
                {
                        "You can reach us through the “Request for A New Card” option from the side bar.\nAlternatively, you may also reach us through the “Contact Us” option from the side bar or\nemail us at general@circle8.asia to request for a replacement card.\nPlease note that a fee of SGD $50.00 will be charged for each replacement card."
                };
        String[] fifthAnswer =
                {
                        "The NFC cards issued and connections established to each account is unique.\nTherefore, your account is securely tied to your card.",
                        "\nIf you require a new account for security or other reasons, we can terminate your existing cards and import your connections after verifying your identity.\nYou will need to complete the acknowledgement slip and return it to us for verification and activation of a new account with your imported contacts."
                };
        String[] sixthAnswer =
                 {
                        "If you do not receive your card within 4 weeks upon registration of the card, please contact us via the “Contact Us” page.\nSelect “Cards” under the dropdown menu and drop us a message with your transaction ID in the message box and we will get back to you within 24 hours."
                 };
        String[] seventhAnswer =
                {
                        "CircleOne provides a handy way to show alerts whenever someone attempts to connect with them or if they receive a message.\nSimilar to other applications, you’ll be able to receive notifications on your mobile with a quick view feature."
                };
        String[] eightAnqwer =
                {
                       "You can sort contacts into groups of your own preference.\nSort them according to industry, interests or backgrounds, the choice is yours.\nTo group your contacts, proceed with the\nfollowing steps:",
                       "1) Go to “Circles” in the sidebar menu.",
                       "2) Click on the “+” symbol to create a new group.",
                       "3) Enter a name or tag as well as a description of your group.",
                       "4) You can modify the groups anytime you want.",
                       "5) To delete a group click “delete” from the options drop bar on the top right."
                };
        String[] ninethAnswer =
                {
                       "Our subscription rates are as follows:",
                       "1) Default Subscription – Free with 200 contacts and up to 4 circles.",
                       "2) Package 1 – SGD$1.00/month for 500 contacts and up to 100000 circles.",
                       "3) Package 2 – SGD$5.00/month for 500 contacts and up to 100000 circles.",
                       "4) Package 3 – SGD$10.00/month for 1000 contacts and up to 100000 circles.",
                       "5) Ultimate Package – SGD$30.00/month for 10000 contacts and up to 100000 circles.",
                       "Our subscriptions are auto-renewable.\nThis means that you’ll need to opt out of our subscriptions manually if you wish to terminate our services.\nSimply select “Billing” under “Contact” and let us know of your decision to cancel your subscription.\n(This isn’t in the app, how do they opt out?)"
                };
        String[] tenthanswer =
                {
                        "The “History” tab gives you a summary of your activities, with all your events highlighted.\nIn there, you will find that all your events have been logged and you can search for a particular activity by keying in keywords in the search bar anytime you like."
                };
        String[] eleventhAnswer =
                {
                        "Security deals with all aspects related to the protection of sensitive information in your account.\nWe currently utilise fingerprint authentication on top of traditional password-and-username verification.\nThe single-touch sign-in provides convenience and added security as your fingerprint is unique to you.",
                        "To “register” a fingerprint with your device, navigate to your app’s security screen.\nWhen the screen prompts you to place your finger on the sensor, place your desired finger for your fingerprint to be locked in, just as you would on your phone home screen.",
                        "Once the sensor has fully captured your fingerprint, tap the ‘Enter’ key on your app to finish the process.\nA notification will appear to confirm that you’ve successfully registered a new fingerprint."
                };
        String[] twelveAnswer =
                {
                        "Here’s how you can sort and filter your contacts in CircleOne quickly.\nClick on the drop-down menu in the top right corner of your device, select the attribute you wish to sort by and it will filter accordingly.",
                        "All Cards: This basically searches through all your contacts in your database.",
                        "Shared Cards: This option will only sift through contacts that you have shared with other people or contacts that have been shared with you.",
                        "Sort by Recently Added: This arranges your contacts by when they were added, with most recent at the top followed by the next and so forth.",
                        "Sort by Name: The contact list default sort order starts with A. However, there is an option to begin searches from Z through to A.",
                        "by Company: This allows you to sort by name of the Organization.",
                        "by Title: This sorts your list of contacts by their job titles.",
                        "by Industry: This filter your contacts by specific industries. Simply enter the industry you would like to find within the search bar.",
                        "by Association:"
                };
        String[] thirteenAnswer =
                {
                        "By default, your profile lists both your personal and professional details.\nYou can choose to create additional profiles if you have more than one personal/professional identity.",
                        "Your profile contains the following fields:",
                        "Name: Your given name will be displayed here.",
                        "Company: The name of the company you are associated with appear here.",
                        "Industry: The nature of business you are in.",
                        "Title: Your position within your company.",
                        "Association: Any or all associations you are involved with.",
                        "Location: Address of your current company.",
                        "Website: URL of your company’s website.",
                        "Email: Your professional email address.",
                        "Phone: Your mobile number or your office number.",
                        "Fax: Fax number.",
                        "Social Media Profiles: Collate your various social media links on your main profile – Facebook/Linkedin/Twitter/Google+.",
                        "Personal Profile: Introduce yourself, write a short bio about yourself to set yourself apart from the crowd.",
                        "Company Profile: Share with other users what your company does."
                };
        String[] fourteenAnswer =
                {
                        "To edit your profile:",
                        "1) Go to your profile tab located at the bottom of your screen, click on the pencil icon to begin editing.",
                        "2) Select the fields that you wish to edit.",
                        "3) Finalise your changes by clicking on \"Save\".",
                };
        String[] fifteenAnswer =
                {
                        "Only your contacts and people who are connected to you can view your complete profile.\nPeople that are not connected to you can only view your profile picture, name and title."
                };
        String[] sixteenAnswer =
                {
                        "For Participants:",
                        "You can view a full list of participating events by tapping on the events icon located at the bottom of your page.\nOnce in there, you will be able to view upcoming events announcements, updates and choose to sign up directly for an event via our app.",
                        "Through the app you will be able to engage with other people that have registered for the event.\nYou will be able to view a comprehensive list of participants that have registered for the event via our app.",
                        "For Event Organizers:",
                        "Please email us at: general@circle8.asia for further enquires or please contact us via the contact form on the app.\nDue to a high volume of organizers requesting to be listed on our app, please bear with us if we take some time to get back to you."
                };
        String[] seventeen =
                {
                        "Connect enables you to add multiple new connections simply by tapping on your network’s extended contacts.",
                        "1) Connect with others by looking up their name/title/company and our platform will match you with your intended connection.",
                        "2) If your intended connection is an immediate contact of your current network, you will be allowed to send them a request to connect with them directly.",
                        "3) If the intended connection is an unknown contact outside of your current network, our system will build a path for you that allows you to connect with that particular individual.\nWe are proud to say we are the only platform that allows you to connect up to six degrees outside your own immediate connections.\nThis basically allows you to connect with anyone in the world (based on Frigyes Karinthy’s idea of the Six Degrees of Separation).",
                        "4) You will need to unlock each level of connection in order to move on to the next – each connection costs SGD$0.99.",
                        "You will also receive invitations to be connected.\nSimply accept the invitation and that person will be added to your network immediately."
                };
        String[] eighteen =
                {
                        "1) Under ‘My Account’, tap on the pencil icon",
                        "2) Here you can update your contact number, email as well as your profile pitcture" ,
                        "3) * fields are compulsory and have to be filled before saving",
                        "4) Tap ‘Save’ once you’re done to finalise changes"
                };
        String[] nineteen =
                {
                        "1) Under ‘My Account’, tap on the pencil icon",
                        "2) Tap on the masked field below your name to change your password",
                        "3) Re-enter your new password in the field directly below it",
                        "4) Tap ‘Save’ once you’re done to finalise changes"
                };
        String[] twenty =
                {
                        "CircleOne works for those whose mobile devices aren’t equipped with NFC capabilities too.\nEach user has a unique QR code beside their name on their Profile.\nThe ‘Profile’ tab can be found on the bottom right of the screen in the dashboard.\nTo access your QR code for another user to scan, simply tap on it to enlarge the image."
                };
        String[] twentyOne =
                {
                        "The ‘Sync Contact’ tab in the sidebar allows you to sync your contacts from your device on to the CircleOne app.\nDoing so allows you to connect better with other users, especially if the both of you are already acquainted, but have yet to connect via the CircleOne app."
                };
        String[] twentyTwo =
                {
                        "Points can be earned through a variety of actions.\nThese accumulated points can be spent or exchanged for discounts and services at our various CircleOne merchants.\nYou’ll be able to see how many points you’ve earned and how you’ve earned it in this tab.\nYou can also view the various merchants and rewards we have by tapping on ‘Merchants’."
                };

        collectionList = new LinkedHashMap<String, List<String>>();

        for (String question : questionsList)
        {
            if (question.equals("My Account:"))
            {
//                loadChild(firstAnswer);
            }
            else if (question.equals("Lost or Stolen Cards:"))
            {
                loadChild(secondAnswer);
            }
            else if (question.equals("Request for a New Card:"))
            {
                loadChild(thirdAnswer);
            }
            else if (question.equals("Damaged Cards:"))
            {
                loadChild(forthAnswer);
            }
            else if (question.equals("Request for a new Account:"))
            {
                loadChild(fifthAnswer);
            }
            else if (question.equals("Requested Card/Cards did not arrive:"))
            {
                loadChild(sixthAnswer);
            }
            else if (question.equals("Notifications:"))
            {
                loadChild(seventhAnswer);
            }
            else if (question.equals("Circles:"))
            {
                loadChild(eightAnqwer);
            }
            else if (question.equals("Subscription:"))
            {
                loadChild(ninethAnswer);
            }
            else if (question.equals("History:"))
            {
                loadChild(tenthanswer);
            }
            else if (question.equals("Security:"))
            {
                loadChild(eleventhAnswer);
            }
            else if (question.equals("Sort and Filter:"))
            {
                loadChild(twelveAnswer);
            }
            else if (question.equals("Profile:"))
            {
                loadChild(thirteenAnswer);
            }
            else if (question.equals("How do I edit my Profile?"))
            {
                loadChild(fourteenAnswer);
            }
            else if (question.equals("Who can access the information on my profile?"))
            {
                loadChild(fifteenAnswer);
            }
            else if (question.equals("Event:"))
            {
                loadChild(sixteenAnswer);
            }
            else if (question.equals("Connect:"))
            {
                loadChild(seventeen);
            }
            else if (question.equals("Editing Your Profile:"))
            {
                loadChild(eighteen);
            }
            else if (question.equals("Changing Your Password:"))
            {
                loadChild(nineteen);
            }
            else if (question.equals("Scan QR Code:"))
            {
                loadChild(twenty);
            }
            else if (question.equals("Sync Contacts:"))
            {
                loadChild(twentyOne);
            }
            else if (question.equals("Reward Points:"))
            {
                loadChild(twentyTwo);
            }

            collectionList.put(question, answersList);
        }
    }

    private void loadChild(String[] laptopModels)
    {
        Utility.freeMemory();
        answersList = new ArrayList<String>();

        for (String model : laptopModels)
        {
            answersList.add(model);
        }
    }

}
