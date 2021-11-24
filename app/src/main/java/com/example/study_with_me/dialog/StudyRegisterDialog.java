package com.example.study_with_me.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.activity.StudyRegisterActivity;

public class StudyRegisterDialog extends Dialog implements View.OnClickListener {
    private Button positiveBtn;
    private Button negativeBtn;
    private EditText etcTextContent;
    private TextView etcGuideText;
    private Context context;
    private StudyRegisterActivity.etcType type;

    public StudyRegisterDialogListener studyRegisterDialogListener;

    public StudyRegisterDialog(Context context, StudyRegisterActivity.etcType type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    /** 인터페이스 설정 **/
    public interface StudyRegisterDialogListener {
        void onPositiveClicked(String content);
        void onNegativeClicked();
    }

    /** 호출할 리스너 초기화 **/
    public void setDialogListener(StudyRegisterDialogListener studyRegisterDialogListener) {
        this.studyRegisterDialogListener = studyRegisterDialogListener;
    }
    
    /** 종류에 따른 안내메시지 설정 **/
    public void setEtcGuideText() {
        switch (type) {
            case CATEGORY:
                etcGuideText.setText("스터디 분류를 입력해주세요");
                break;
            case PEOPLE:
                etcGuideText.setText("스터디 인원을 입력해주세요");
                break;
            case DATE:
                etcGuideText.setText("스터디 기간을 입력해주세요. \nyyyy/mm/dd 형식으로 입력해주세요");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ect_pop_up);

        /** 초기화 **/
        positiveBtn = (Button) findViewById(R.id.etcPositiveBtn);
        negativeBtn = (Button) findViewById(R.id.etcNegativeBtn);
        etcTextContent = (EditText) findViewById(R.id.etcText);
        etcGuideText = (TextView) findViewById(R.id.etcGuideText);
        setEtcGuideText();
        
        /** 버튼 클릭 리스너 등록 **/
        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.etcPositiveBtn:
                String etcContent = etcTextContent.getText().toString();
                studyRegisterDialogListener.onPositiveClicked(etcContent);
                dismiss();
                break;
            case R.id.etcNegativeBtn:
                cancel();
                break;
        }
    }
}
