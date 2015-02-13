package ru.chand.googleimagesearch.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import ru.chand.googleimagesearch.R;
import ru.chand.googleimagesearch.model.SearchOptions;
import ru.chand.googleimagesearch.utilities.Constants;

/**
 * Created by chandrav on 2/12/15.
 */
public class EditOptionsDialog extends DialogFragment {

    private Button btnSave;
    private Button btnCancel;

    public EditOptionsDialog() {
        // Empty constructor required for DialogFragment
    }

    public static EditOptionsDialog newInstance(String title, SearchOptions searchOptions) {
        EditOptionsDialog frag = new EditOptionsDialog();
        Bundle args = new Bundle();
        args.putString(Constants.FRAGEMENT_PARAMETER_TITLE, title);
        args.putSerializable(Constants.FRAGEMENT_PARAMETER_SEARCH_OPTIONS,searchOptions );
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_options, container);
        String title = getArguments().getString(Constants.FRAGEMENT_PARAMETER_TITLE, getString(R.string.settingsTitle));
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btnSave = (Button) view.findViewById(R.id.btnOk);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArguments().putString("TEST", "TEST VALUE");
                dismiss();
                
            }
        });
        
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        
        
        

        return view;
    }

        
}
