package com.example.rmmconlineexam.model;

import android.content.Context;

import com.example.rmmconlineexam.R;
import com.example.rmmconlineexam.util.Messenger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Student {

    @SerializedName("student_id")
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("birthdate")
    private String birthdate;
    @SerializedName("contact_number")
    private String contactNumber;
    @SerializedName("course")
    private String course;
    @SerializedName("gender")
    private String gender;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("is_activated")
    private int isActivated;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGender() {
        return gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int isActivated() {
        return isActivated;
    }

    public void setActivated(int activated) {
        isActivated = activated;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }




    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("first_name", firstName);
            jsonObject.put("middle_name", middleName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("email", email);
            jsonObject.put("birthdate", birthdate);
            jsonObject.put("contact_number", contactNumber);
            jsonObject.put("course", course);
            jsonObject.put("gender", gender);


            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    public boolean validateField(Context context, String title) {
        if (firstName == null || firstName.trim().isEmpty()) {
            showDataError(context, title, "First name is required");
            return false;
        }

        if (middleName == null || middleName.trim().isEmpty()) {
            showDataError(context, title, "Middle name is required");
            return false;
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            showDataError(context, title, "Last name is required");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            showDataError(context, title, "Email is required");
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showDataError(context, title, "Invalid email format");
            return false;
        }

        if (birthdate == null || birthdate.trim().isEmpty()) {
            showDataError(context, title, "Birthdate is required");
            return false;
        }
        if (!birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            showDataError(context, title, "Birthdate must be in the format YYYY-MM-DD");
            return false;
        }

        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            showDataError(context, title, "Contact number is required");
            return false;
        }
        if (!contactNumber.matches("\\d{11}")) {
            showDataError(context, title, "Contact number must be exactly 11 digits");
            return false;
        }
        if (course == null || course.trim().isEmpty()) {
            showDataError(context, title, "Course is required");
            return false;
        }

        if (gender == null || gender.trim().isEmpty()) {
            showDataError(context, title, "Gender is required");
            return false;
        }
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            showDataError(context, title, "Gender must be either 'Male' or 'Female'");
            return false;
        }

        return true;
    }


    private void showDataError(Context context, String title, String message){
        Messenger.showAlertDialog(context, title, message, "Ok").show();
    }


}
