// Generated by view binder compiler. Do not edit!
package com.example.blogsystem.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import com.example.blogsystem.R;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentRegisterBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final Button buttonBack;

  @NonNull
  public final Button buttonSignUp;

  @NonNull
  public final Button buttonVerifyCode;

  @NonNull
  public final Guideline guideline1;

  @NonNull
  public final Guideline guideline4;

  @NonNull
  public final Guideline guideline5;

  @NonNull
  public final TextInputLayout inputLayoutAccount;

  @NonNull
  public final TextInputLayout inputLayoutPassword;

  @NonNull
  public final TextInputLayout inputLayoutVerifyCode;

  @NonNull
  public final TextInputLayout reInputLayoutPassword;

  private FragmentRegisterBinding(@NonNull FrameLayout rootView, @NonNull Button buttonBack,
      @NonNull Button buttonSignUp, @NonNull Button buttonVerifyCode, @NonNull Guideline guideline1,
      @NonNull Guideline guideline4, @NonNull Guideline guideline5,
      @NonNull TextInputLayout inputLayoutAccount, @NonNull TextInputLayout inputLayoutPassword,
      @NonNull TextInputLayout inputLayoutVerifyCode,
      @NonNull TextInputLayout reInputLayoutPassword) {
    this.rootView = rootView;
    this.buttonBack = buttonBack;
    this.buttonSignUp = buttonSignUp;
    this.buttonVerifyCode = buttonVerifyCode;
    this.guideline1 = guideline1;
    this.guideline4 = guideline4;
    this.guideline5 = guideline5;
    this.inputLayoutAccount = inputLayoutAccount;
    this.inputLayoutPassword = inputLayoutPassword;
    this.inputLayoutVerifyCode = inputLayoutVerifyCode;
    this.reInputLayoutPassword = reInputLayoutPassword;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button_back;
      Button buttonBack = rootView.findViewById(id);
      if (buttonBack == null) {
        break missingId;
      }

      id = R.id.button_sign_up;
      Button buttonSignUp = rootView.findViewById(id);
      if (buttonSignUp == null) {
        break missingId;
      }

      id = R.id.button_verify_code;
      Button buttonVerifyCode = rootView.findViewById(id);
      if (buttonVerifyCode == null) {
        break missingId;
      }

      id = R.id.guideline1;
      Guideline guideline1 = rootView.findViewById(id);
      if (guideline1 == null) {
        break missingId;
      }

      id = R.id.guideline4;
      Guideline guideline4 = rootView.findViewById(id);
      if (guideline4 == null) {
        break missingId;
      }

      id = R.id.guideline5;
      Guideline guideline5 = rootView.findViewById(id);
      if (guideline5 == null) {
        break missingId;
      }

      id = R.id.input_layout_account;
      TextInputLayout inputLayoutAccount = rootView.findViewById(id);
      if (inputLayoutAccount == null) {
        break missingId;
      }

      id = R.id.input_layout_password;
      TextInputLayout inputLayoutPassword = rootView.findViewById(id);
      if (inputLayoutPassword == null) {
        break missingId;
      }

      id = R.id.input_layout_verify_code;
      TextInputLayout inputLayoutVerifyCode = rootView.findViewById(id);
      if (inputLayoutVerifyCode == null) {
        break missingId;
      }

      id = R.id.reInput_layout_password;
      TextInputLayout reInputLayoutPassword = rootView.findViewById(id);
      if (reInputLayoutPassword == null) {
        break missingId;
      }

      return new FragmentRegisterBinding((FrameLayout) rootView, buttonBack, buttonSignUp,
          buttonVerifyCode, guideline1, guideline4, guideline5, inputLayoutAccount,
          inputLayoutPassword, inputLayoutVerifyCode, reInputLayoutPassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
