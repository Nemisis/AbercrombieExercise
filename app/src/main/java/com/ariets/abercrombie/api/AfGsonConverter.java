package com.ariets.abercrombie.api;

import android.content.Context;

import com.ariets.abercrombie.PreferenceUtils;
import com.ariets.abercrombie.model.AfPromotion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class AfGsonConverter implements Converter {
    private final Gson gson;
    private final String charset;
    private Context context;

    public AfGsonConverter(Context context, JsonDeserializer deserializer, Type type) {
        this.context = context;
        this.charset = "UTF-8";
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(type, deserializer)
                .create();
    }

    public static Type getType() {
        return new TypeToken<ArrayList<AfPromotion>>() {
        }.getType();
    }

    public static JsonDeserializer getPromotionDeserializer() {
        return new AfPromotion.Deserializer();
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        String charset = this.charset;
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType(), charset);
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(body.in(), charset);
            // Convert to JSON for the purpose of cacheing offline.
            String jsonStr = convertToStringForSavingInPreferences(isr);
            return gson.fromJson(jsonStr, type);
        } catch (IOException e) {
            throw new ConversionException(e);
        } catch (JsonParseException e) {
            throw new ConversionException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private String convertToStringForSavingInPreferences(InputStreamReader isr) throws IOException {
        BufferedReader r = new BufferedReader(isr);
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        String json = total.toString();
        if (json.length() > 0) {
            PreferenceUtils.setJsonData(context, json);
        }
        return json;
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            JsonTypedOutput output = new JsonTypedOutput(gson.toJson(object).getBytes(charset), charset);
            return output;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }

}
