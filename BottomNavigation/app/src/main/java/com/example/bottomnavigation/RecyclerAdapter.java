package com.example.bottomnavigation;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<ContactItem> contactlist = new ArrayList<>();

    private ImageButton btnTest; // 테스트용 버튼
    private ImageButton removeBtn;
    private final Context mContext;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    private OnItemClickListener removeItemClickListener;

    public RecyclerAdapter(Context context, OnItemClickListener onItemClickListener, OnItemClickListener removeItemClickListener){
        mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.removeItemClickListener = removeItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        final int Position = position;
        holder.onBind(contactlist.get(position));
        RecyclerAdapter.ItemViewHolder vholder = (RecyclerAdapter.ItemViewHolder)holder;

        vholder.getBtnTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, Position);
            }
        });

        vholder.getremoveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemClickListener.onItemClick(v, Position);
            }
        });
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return contactlist.size();
    }

    void addItem(ContactItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        contactlist.add(data);
    }

    void resetItem(){
        contactlist.clear();
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            btnTest  = itemView.findViewById(R.id.call_button);
            removeBtn = itemView.findViewById(R.id.remove_button);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public ImageButton getBtnTest() {
            return btnTest;
        }
        public ImageButton getremoveBtn() {
            return removeBtn;
        }

        void onBind(ContactItem contactItem) {
//            textView1.setText(data.getTitle());
//            textView2.setText(data.getContent());
//            imageView.setImageResource(data.getResId());
            textView1.setText(contactItem.getUser_Name());
            textView2.setText(contactItem.getUser_phNumber());
            Bitmap profile = loadContactPhoto(mContext.getContentResolver(),contactItem.getPerson_id(), contactItem
                    .getPhoto_id());
            if (profile != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    imageView.setBackground(new ShapeDrawable(new OvalShape()));
                    imageView.setClipToOutline(true);
                }
                imageView.setImageBitmap(profile);
            } else {
//            viewHolder.profile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_profile_thumnail));
                if (Build.VERSION.SDK_INT >= 21) {
                    imageView.setClipToOutline(false);
                }
            }
        }
    }

    public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id) {
//        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
//        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
//        if (input != null)
//            return resizingBitmap(BitmapFactory.decodeStream(input));
//        else
//            Log.d("<<CONTACT_PHOTO>>", "first try failed to load photo");

        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                null,null, null);
        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        if (photoBytes != null) {
            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));
        } else
            Log.d("<<CONTACT_PHOTO>>", "second try also failed");

        return null;

    }

    public Bitmap resizingBitmap(Bitmap oBitmap) {
        if (oBitmap == null) {
            return null;
        }

        float width = oBitmap.getWidth();
        float height = oBitmap.getHeight();
        float resizing_size = 120;

        Bitmap rBitmap = null;
        if (width > resizing_size) {
            float mWidth = (float)(width / 100);
            float fScale = (float)(resizing_size / mWidth);
            width *= (fScale / 100);
            height *= (fScale / 100);

        } else if (height > resizing_size) {
            float mHeight = (float)(height / 100);
            float fScale = (float)(resizing_size / mHeight);

            width *= (fScale / 100);
            height *= (fScale / 100);
        }

        //Log.d("rBitmap : " + width + ", " + height);

        rBitmap = Bitmap.createScaledBitmap(oBitmap, (int)width, (int)height, true);
        return rBitmap;
    }
}
