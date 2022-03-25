package com.apponative.committee_app.utils;

import com.apponative.committee_app.R;

import java.util.HashMap;

/**
 * Created by Muhammad Waqas on 4/20/2017.
 */

public class Constants {
    public static final int PICK_FROM_CAMERA = 1;
    public static final int PICK_FROM_GALLERY = 2;

    //Firebase User References
    public static final String TBL_USER = "users";
    public static final String FLD_TOKEN = "token";

    //Firebase Contacts References
    public static final String TBL_CONTACTS = "user_contacts";
    public static final String FLD_CONTACT = "_contacts";

    //Firebase Committee References

    public static final String TBL_COMS = "allcommittees";
    public static final String TBL_USERCOMS = "user_committees";
    public static final String FLD_COMS = "_committees";
    public static final String TBL_COM_MEMBERS = "committee_members";

    public static final String NEWINVITES = "new_invites";  //new invites
    public static final String PENDING = "pending";  // pending
    public static final String ONGOING = "on_going";  //ongoing
    public static final String COMPLETED = "completed";  //completed
    public static final String JOINED = "joined";

    //Firebase Notification Reference

    public static final String TBL_INVITENOTICE = "CommitteeRequests";

    public static final String TBL_NOTIFICATION = "generalNotifications";
    //Firebase Storage Path

    public static final String ST_PP = "images/user_";
    public static final String PP_TYPE = ".jpg";


    //App Keys

    public static final String F_COUNT = "count";
    public static final String C_TYPE = "isDetail";
    public static final String C_TYPE1 = "isDetail1";
    // Alert Message

    public static final String INCOMPLETE_FIELDS = "Please fill all fields";
    public static final String CAL_RES = "Amount to be Paid per month is ";

    //Share Message

    public static final String SHARE_APP = "I invite you to Committee App. Please install from the following link:";

    //Permissions
    public static final int PERMISSION_REQUEST_CAMERA = 1;
    public static final int PERMISSION_REQUEST_CALL = 3;
    public static final int PERMISSION_REQUEST_SMS = 4;
    public static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 2;
    public static final int PERMISSION_REQUEST_LOCATION = 5;
    public static final int PERMISSION_REQUEST_CONTACTS = 6;

    // Turn Statuses
    public static int PAST = 0;
    public static int CURRENT = 1;
    public static int FUTURE = 2;


    //SharedPref Keys

    public static final String ISNEW = "isNew";
    public static final String NOTIFICATIONSLIST = "notificationlist";
    public static final String TRANSLATED = "translated";
    public static final String PROTECTED = "protected";
    public static final String AUTOREMINDER = "autoreminder";
    public static final String PASSCODE = "passcode";

    //Notificaion Types:
    public static enum NTYPE {
        INVITE,
        JOIN,
        DECLINE,
        CONFIRMED,
        UNCONFIRMED,
        COMPLETED,
        P_STATUS,
        P_REMINDER,
        SHARE_TURN
    }

    public static HashMap<String, Integer> NotificationIntents = new HashMap<>();

    public static void initializeNotificationIntents() {
        NotificationIntents.put(String.valueOf(NTYPE.INVITE), R.string.tag_new_committee);
        NotificationIntents.put(String.valueOf(NTYPE.JOIN), R.string.tag_pending_committee);
        NotificationIntents.put(String.valueOf(NTYPE.DECLINE), R.string.tag_pending_committee);
        NotificationIntents.put(String.valueOf(NTYPE.CONFIRMED), R.string.tag_new_committee);
        NotificationIntents.put(String.valueOf(NTYPE.UNCONFIRMED), R.string.tag_new_committee);
        NotificationIntents.put(String.valueOf(NTYPE.COMPLETED), R.string.tag_committee_detail);
        NotificationIntents.put(String.valueOf(NTYPE.P_STATUS), R.string.tag_ongoing_detail);
        NotificationIntents.put(String.valueOf(NTYPE.P_REMINDER), R.string.tag_ongoing_detail);
        NotificationIntents.put(String.valueOf(NTYPE.SHARE_TURN), R.string.tag_ongoing_detail);
    }

    public static HashMap<String, String> countrymap = new HashMap<>();

    public static void initializeCountryCodes() {
        countrymap.put("AF", "93");
        countrymap.put("AL", "355");
        countrymap.put("DZ", "213");
        countrymap.put("AD", "376");
        countrymap.put("AO", "244");
        countrymap.put("AQ", "672");
        countrymap.put("AR", "54");
        countrymap.put("AM", "374");
        countrymap.put("AW", "297");
        countrymap.put("AU", "61");
        countrymap.put("AT", "43");
        countrymap.put("AZ", "994");
        countrymap.put("BH", "973");
        countrymap.put("BD", "880");
        countrymap.put("BY", "375");
        countrymap.put("BE", "32");
        countrymap.put("BZ", "501");
        countrymap.put("BJ", "229");
        countrymap.put("BT", "975");
        countrymap.put("BO", "591");
        countrymap.put("BA", "387");
        countrymap.put("BW", "267");
        countrymap.put("BR", "55");
        countrymap.put("BN", "673");
        countrymap.put("BG", "359");
        countrymap.put("BF", "226");
        countrymap.put("MM", "95");
        countrymap.put("BI", "257");
        countrymap.put("KH", "855");
        countrymap.put("CM", "237");
        countrymap.put("CA", "1");
        countrymap.put("CV", "238");
        countrymap.put("CF", "236");
        countrymap.put("TD", "235");
        countrymap.put("CL", "56");
        countrymap.put("CN", "86");
        countrymap.put("CX", "61");
        countrymap.put("CC", "61");
        countrymap.put("CO", "57");
        countrymap.put("KM", "269");
        countrymap.put("CG", "242");
        countrymap.put("CD", "243");
        countrymap.put("CK", "682");
        countrymap.put("CR", "506");
        countrymap.put("HR", "385");
        countrymap.put("CU", "53");
        countrymap.put("CY", "357");
        countrymap.put("CZ", "420");
        countrymap.put("DK", "45");
        countrymap.put("DJ", "253");
        countrymap.put("TL", "670");
        countrymap.put("EC", "593");
        countrymap.put("EG", "20");
        countrymap.put("SV", "503");
        countrymap.put("GQ", "240");
        countrymap.put("ER", "291");
        countrymap.put("EE", "372");
        countrymap.put("ET", "251");
        countrymap.put("FK", "500");
        countrymap.put("FO", "298");
        countrymap.put("FJ", "679");
        countrymap.put("FI", "358");
        countrymap.put("FR", "33");
        countrymap.put("PF", "689");
        countrymap.put("GA", "241");
        countrymap.put("GM", "220");
        countrymap.put("GE", "995");
        countrymap.put("DE", "49");
        countrymap.put("GH", "233");
        countrymap.put("GI", "350");
        countrymap.put("GR", "30");
        countrymap.put("GL", "299");
        countrymap.put("GT", "502");
        countrymap.put("GN", "224");
        countrymap.put("GW", "245");
        countrymap.put("GY", "592");
        countrymap.put("HT", "509");
        countrymap.put("HN", "504");
        countrymap.put("HK", "852");
        countrymap.put("HU", "36");
        countrymap.put("IN", "91");
        countrymap.put("ID", "62");
        countrymap.put("IR", "98");
        countrymap.put("IQ", "964");
        countrymap.put("IE", "353");
        countrymap.put("IM", "44");
        countrymap.put("IL", "972");
        countrymap.put("IT", "39");
        countrymap.put("CI", "225");
        countrymap.put("JP", "81");
        countrymap.put("JO", "962");
        countrymap.put("KZ", "7");
        countrymap.put("KE", "254");
        countrymap.put("KI", "686");
        countrymap.put("KW", "965");
        countrymap.put("KG", "996");
        countrymap.put("LA", "856");
        countrymap.put("LV", "371");
        countrymap.put("LB", "961");
        countrymap.put("LS", "266");
        countrymap.put("LR", "231");
        countrymap.put("LY", "218");
        countrymap.put("LI", "423");
        countrymap.put("LT", "370");
        countrymap.put("LU", "352");
        countrymap.put("MO", "853");
        countrymap.put("MK", "389");
        countrymap.put("MG", "261");
        countrymap.put("MW", "265");
        countrymap.put("MY", "60");
        countrymap.put("MV", "960");
        countrymap.put("ML", "223");
        countrymap.put("MT", "356");
        countrymap.put("MH", "692");
        countrymap.put("MR", "222");
        countrymap.put("MU", "230");
        countrymap.put("YT", "262");
        countrymap.put("MX", "52");
        countrymap.put("FM", "691");
        countrymap.put("MD", "373");
        countrymap.put("MC", "377");
        countrymap.put("MN", "976");
        countrymap.put("ME", "382");
        countrymap.put("MA", "212");
        countrymap.put("MZ", "258");
        countrymap.put("NA", "264");
        countrymap.put("NR", "674");
        countrymap.put("NP", "977");
        countrymap.put("NL", "31");
        countrymap.put("AN", "599");
        countrymap.put("NC", "687");
        countrymap.put("NZ", "64");
        countrymap.put("NI", "505");
        countrymap.put("NE", "227");
        countrymap.put("NG", "234");
        countrymap.put("NU", "683");
        countrymap.put("KP", "850");
        countrymap.put("NO", "47");
        countrymap.put("OM", "968");
        countrymap.put("PK", "92");
        countrymap.put("PW", "680");
        countrymap.put("PA", "507");
        countrymap.put("PG", "675");
        countrymap.put("PY", "595");
        countrymap.put("PE", "51");
        countrymap.put("PH", "63");
        countrymap.put("PN", "870");
        countrymap.put("PL", "48");
        countrymap.put("PT", "351");
        countrymap.put("PR", "1");
        countrymap.put("QA", "974");
        countrymap.put("RO", "40");
        countrymap.put("RU", "7");
        countrymap.put("RW", "250");
        countrymap.put("BL", "590");
        countrymap.put("WS", "685");
        countrymap.put("SM", "378");
        countrymap.put("ST", "239");
        countrymap.put("SA", "966");
        countrymap.put("SN", "221");
        countrymap.put("RS", "381");
        countrymap.put("SC", "248");
        countrymap.put("SL", "232");
        countrymap.put("SG", "65");
        countrymap.put("SK", "421");
        countrymap.put("SI", "386");
        countrymap.put("SB", "677");
        countrymap.put("SO", "252");
        countrymap.put("ZA", "27");
        countrymap.put("KR", "82");
        countrymap.put("ES", "34");
        countrymap.put("LK", "94");
        countrymap.put("SH", "290");
        countrymap.put("PM", "508");
        countrymap.put("SD", "249");
        countrymap.put("SR", "597");
        countrymap.put("SZ", "268");
        countrymap.put("SE", "46");
        countrymap.put("CH", "41");
        countrymap.put("SY", "963");
        countrymap.put("TW", "886");
        countrymap.put("TJ", "992");
        countrymap.put("TZ", "255");
        countrymap.put("TH", "66");
        countrymap.put("TG", "228");
        countrymap.put("TK", "690");
        countrymap.put("TO", "676");
        countrymap.put("TN", "216");
        countrymap.put("TR", "90");
        countrymap.put("TM", "993");
        countrymap.put("TV", "688");
        countrymap.put("AE", "971");
        countrymap.put("UG", "256");
        countrymap.put("GB", "44");
        countrymap.put("UA", "380");
        countrymap.put("UY", "598");
        countrymap.put("US", "1");
        countrymap.put("UZ", "998");
        countrymap.put("VU", "678");
        countrymap.put("VA", "39");
        countrymap.put("VE", "58");
        countrymap.put("VN", "84");
        countrymap.put("WF", "681");
        countrymap.put("YE", "967");
        countrymap.put("ZM", "260");
        countrymap.put("ZW", "263");


    }

}
