package com.example.meallogin;
import org.junit.Test;

import static org.junit.Assert.*;
public class ComplaintUnitTest {
    public void isComplaint(){
        Complaint c = new Complaint("Hello","Name");
        assertEquals(c.getDescription(), "Hello");
    }
}
