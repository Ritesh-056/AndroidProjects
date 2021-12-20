package np.com.socialize.category;

import java.util.HashMap;
import java.util.Map;

public class User {

     String name;
     String phoneNumber;
     String gender;
     String profile_photo;
     String dateofBirth;
     String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        if (name != null) {
            map.put("name", name);
        }
        if (phoneNumber != null) {
            map.put("phoneNumber", phoneNumber);
        }
        if (gender != null) {
            map.put("gender", gender);
        }

        if (profile_photo !=null){

            map.put("profile_photo",profile_photo);
        }

        if (dateofBirth != null){

            map.put("date_of_birth",dateofBirth);
        }

        return map;
    }


}
