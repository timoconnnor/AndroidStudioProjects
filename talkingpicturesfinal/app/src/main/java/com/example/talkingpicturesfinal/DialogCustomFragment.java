package com.example.talkingpicturesfinal;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.io.File;

public class DialogCustomFragment extends DialogFragment
        implements DialogInterface.OnDismissListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView;
        Uri imageUri;

        Log.i("IN DialogCustomFragment", "onCreateView()");

        dialogView = inflater.inflate(R.layout.dialog, container);
        dialogView.findViewById(R.id.dialog_dismiss).setOnClickListener(myClickHandler);

        // Check if both image URI and description are passed
        String imageUriString = this.getArguments().getString("image_uri");
        String description = this.getArguments().getString("description");

        if (imageUriString != null && !imageUriString.isEmpty()) {
            // Display the image
            dialogView.findViewById(R.id.dialog_image).setVisibility(View.VISIBLE);
            ImageView imageView = dialogView.findViewById(R.id.dialog_image);
            imageView.setImageURI(Uri.parse(imageUriString));
        }

        if (description != null && !description.isEmpty()) {
            // Display the description in the dialog
            dialogView.findViewById(R.id.dialog_description).setVisibility(View.VISIBLE);
            TextView descriptionTextView = dialogView.findViewById(R.id.dialog_description);
            descriptionTextView.setText(description);
        }

        return dialogView;
    }
    public void setImageAndDescription(String imageUriString, String description) {
        Bundle args = new Bundle();
        args.putString("image_uri", imageUriString);
        args.putString("description", description);
        setArguments(args);

        if (getView() != null) {
            // If the view is already created, update the image and description
            ImageView imageView = getView().findViewById(R.id.dialog_image);
            imageView.setImageURI(Uri.parse(imageUriString));
            // You may also update other UI elements if needed
        }
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