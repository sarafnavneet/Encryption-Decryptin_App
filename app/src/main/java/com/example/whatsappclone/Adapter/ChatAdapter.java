package com.example.whatsappclone.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatDetail;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameterGenerator;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.SimpleTimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ChatAdapter extends RecyclerView.Adapter {

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    ArrayList<MessageModel> messageModels;
    Context context;
    private Cipher decipher;
    private final byte[] encryptionKey = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    private SecretKeySpec secretKeySpec;

    public ChatAdapter(ArrayList<MessageModel> list, Context context) {
        this.messageModels = list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view;
        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
        try {
            decipher =Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        if(viewType == SENDER_VIEW_TYPE){
            view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        }

        else{
            view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }


    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessageModel  current_messageModel = messageModels.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current_messageModel.getTimeStamp());
        String date = DateFormat.format("hh:mm", calendar).toString();

        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
        try {
            decipher =Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        String decryptMessage = null;
        try {
            decryptMessage = decrypt(current_messageModel.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder) holder).senderMessage.setText(decryptMessage);
            ((SenderViewHolder) holder).senderTime.setText(date);

        }

        else{
            ((ReceiverViewHolder) holder).receiverMessage.setText(decryptMessage);
            ((ReceiverViewHolder) holder).receiverTime.setText(date);
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMessage, receiverTime;
        public ReceiverViewHolder( View itemView) {
            super(itemView);

            receiverMessage = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMessage, senderTime;

        public SenderViewHolder(View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }

    }


    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getUid().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
            return SENDER_VIEW_TYPE;
        }
        else
            return RECEIVER_VIEW_TYPE;
    }

    private String decrypt(String message) throws UnsupportedEncodingException {
        byte[] encryptedByte = new byte[0];
        encryptedByte = message.getBytes("ISO-8859-1");

        String decryptedString = message;
        byte[] decryptByte = new byte[0];

        try {
            decipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            decryptByte = decipher.doFinal(encryptedByte);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        decryptedString = new String(decryptByte);
        return decryptedString;
    }
}
