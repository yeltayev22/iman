package zerotoonecom.n17r.mubr.Mosques;

/**
 * Created by Nurdaulet Yeltayev (yeltayev22) on 12.08.2017.
 */

public class Mosque {

    private String name;
    private String address;
    private String rating;
    private String phone;
    private String latitude;
    private String longitude;

    public Mosque(String mName, String mAddress, String mPhone, String mRating, String mLatitude, String mLongitude){
        name = mName;
        address = mAddress;
        phone = mPhone;
        rating = mRating;
        latitude = mLatitude;
        longitude = mLongitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getRating() {
        return rating;
    }

    public String getPhone() {
        return phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
