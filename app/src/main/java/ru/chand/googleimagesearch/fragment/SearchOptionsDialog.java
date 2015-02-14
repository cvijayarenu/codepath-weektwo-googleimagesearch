package ru.chand.googleimagesearch.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import ru.chand.googleimagesearch.R;
import ru.chand.googleimagesearch.model.SearchOptions;
import ru.chand.googleimagesearch.utilities.Helper;

/**
 * Created by chandrav on 2/12/15.
 */
public class SearchOptionsDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private Button btnSave;
    private Button btnCancel;
    private SearchOptions searchOptions;
    private Spinner imagesize;
    private Spinner imagecolor;
    private Spinner imagetype;
    private EditText siteFilter;
    

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId){
            SearchOptionsDialogListener listner = (SearchOptionsDialogListener) getActivity();
            listner.onFinishingSearchOptions(searchOptions);
            dismiss();
            return true;
        }
        
        return false;
    }

    public interface SearchOptionsDialogListener {
        void onFinishingSearchOptions(SearchOptions searchOptions);
    }
    
    public SearchOptionsDialog() {
        // Empty constructor required for DialogFragment
    }

    public static SearchOptionsDialog newInstance(String title, SearchOptions searchOptions) {
        SearchOptionsDialog frag = new SearchOptionsDialog();
        Bundle args = new Bundle();
        args.putString(Helper.FRAGEMENT_PARAMETER_TITLE, title);
        args.putSerializable(Helper.FRAGEMENT_PARAMETER_SEARCH_OPTIONS,searchOptions );
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_options, container);
        String title = getArguments().getString(Helper.FRAGEMENT_PARAMETER_TITLE, getString(R.string.settingsTitle));
        searchOptions = (SearchOptions) getArguments().getSerializable(Helper.FRAGEMENT_PARAMETER_SEARCH_OPTIONS);
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        imagesize = (Spinner) view.findViewById(R.id.spImageSize);
        imagecolor = (Spinner) view.findViewById(R.id.spImageColor);
        imagetype = (Spinner) view.findViewById(R.id.spImageType);
        siteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        restoreSearchOptions();
        
        btnSave = (Button) view.findViewById(R.id.btnOk);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAndUpdateSearchOptionsAndNotify();
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
    
    
    private void readAndUpdateSearchOptionsAndNotify(){
        if(imagesize.getSelectedItem().toString().equals("all")){
            searchOptions.delete(SearchOptions.IMAGE_SIZE);
        } else {
            searchOptions.setOption(SearchOptions.IMAGE_SIZE, imagesize.getSelectedItem().toString());
        }
        if(imagetype.getSelectedItem().toString().equals("all")){
            searchOptions.delete(SearchOptions.IMAGE_TYPE);
        } else {
            searchOptions.setOption(SearchOptions.IMAGE_TYPE, imagetype.getSelectedItem().toString());
        }
        if(imagecolor.getSelectedItem().toString().equals("all")){
            searchOptions.delete(SearchOptions.IMAGE_COLOR);
        } else{
            searchOptions.setOption(SearchOptions.IMAGE_COLOR, imagecolor.getSelectedItem().toString());
        }
        if(siteFilter.getText().toString().equals("")){
            searchOptions.delete(SearchOptions.SITE_FILTER);
        } else {
            searchOptions.setOption(SearchOptions.SITE_FILTER, siteFilter.getText().toString());
        }
        
        //pass the object back.
        SearchOptionsDialogListener listener  = (SearchOptionsDialogListener) getActivity();
        listener.onFinishingSearchOptions(searchOptions);

    }
    
    private void restoreSearchOptions(){
        String imageSize = searchOptions.getOption(SearchOptions.IMAGE_SIZE);
        if( imageSize != null){
            setSpinnerToValue(imagesize,imageSize );
        }
        
        String imageColor = searchOptions.getOption(SearchOptions.IMAGE_COLOR);
        if (imageColor != null){
            setSpinnerToValue(imagecolor, imageColor);
        }
        
        String imageType = searchOptions.getOption(SearchOptions.IMAGE_TYPE);
        if(imageType != null){
            setSpinnerToValue(imagetype, imageType);
        }
        
        String sf = searchOptions.getOption(SearchOptions.SITE_FILTER);
        if(sf != null){
            siteFilter.setText(sf);
        }
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }


}
