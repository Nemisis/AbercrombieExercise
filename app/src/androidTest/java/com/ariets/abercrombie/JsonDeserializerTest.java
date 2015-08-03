package com.ariets.abercrombie;

import android.test.suitebuilder.annotation.SmallTest;

import com.ariets.abercrombie.model.AfButton;
import com.ariets.abercrombie.model.AfPromotion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by aaron on 7/31/15.
 */
@SmallTest
public class JsonDeserializerTest {

    private Gson gson;
    private Type type;

    @Before
    public void setup() {
        type = new TypeToken<ArrayList<AfPromotion>>() {
        }.getType();
        gson = new GsonBuilder()
                .registerTypeAdapter(type, new AfPromotion.Deserializer()).create();
    }


    @After
    public void teardow() {
        gson = null;
    }

    @Test
    public void testSerialization() {
        ArrayList<AfPromotion> promotions = gson.fromJson(JSON, type);
        assertNotNull(promotions);
        assertEquals(2, promotions.size());

        // Ensure that the buttons serialized properly
        AfPromotion promotion1 = promotions.get(0);
        assertNotNull(promotion1.getButton());

        AfPromotion promotion2 = promotions.get(1);
        ArrayList<AfButton> buttons = promotion2.getButtonList();
        assertNotNull(buttons);
        assertEquals(1, buttons.size());
    }

    private static final String JSON = "{\"promotions\":[{\"button\":{\"target\":\"https://m.abercrombie.com\"," +
            "\"title\":\"Shop Now\"},\"description\":\"GET READY FOR SUMMER DAYS\",\"footer\":\"In stores & online. " +
            "Exclusions apply. <a href=\\\"https://www.abercrombie" +
            ".com/anf/media/legalText/viewDetailsText20150618_Shorts25_US.html\\\" class=\\\"legal " +
            "promo-details\\\">See details</a>\",\"image\":\"http://anf.scene7" +
            ".com/is/image/anf/anf-US-20150629-app-women-shorts\",\"title\":\"Shorts Starting at $25\"}," +
            "{\"button\":[{\"target\":\"https://m.hollisterco.com\",\"title\":\"Shop Now\"}],\"description\":\"Our " +
            "Favorite Brands\",\"image\":\"http://anf.scene7.com/is/image/anf/anf-US-20150629-app-women-brands\"," +
            "\"title\":\"Dolce Vita\"}]}";

}
