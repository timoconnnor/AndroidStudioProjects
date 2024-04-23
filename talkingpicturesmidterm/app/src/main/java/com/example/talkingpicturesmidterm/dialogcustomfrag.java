package com.example.talkingpicturesmidterm;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class DialogCustomFrag extends DialogFragment
        implements DialogInterface.OnDismissListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View dialogView;
        Uri imageUri;

        Log.i("IN DialogCustomFragment", "onCreateView()");

        dialogView = inflater.inflate(R.layout.dialog,container);
        dialogView.findViewById(R.id.dialog_dismiss)
                .setOnClickListener(myClickHandler);
        imageUri = Uri.parse(this.getArguments().getString("image_uri"));
        ImageView imageView = dialogView.findViewById(R.id.dialog_image);
        imageView.setImageURI(imageUri);
        return(dialogView);
    }

    private View.OnClickListener myClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_dismiss:
                    Log.i("IN DialogCustomFragment", "onClick(): case R.id.dialog_dismiss");
                    dismiss();
                    break;
                default:
                    Log.i("ERROR", "IN DialogCustomFragment: onClick() default case");
                    break;
            }
        }
    };

    public interface StopTalking {

        public void stopTalking();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);

        Log.i("IN DialogCustomFragment", "onDismiss()");

        StopTalking myActivitiy = (StopTalking) getActivity();
        myActivitiy.stopTalking();
    }
}
