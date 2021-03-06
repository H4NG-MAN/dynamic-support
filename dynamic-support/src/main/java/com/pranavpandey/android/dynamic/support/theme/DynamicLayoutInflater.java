/*
 * Copyright 2019 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.support.theme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomAppBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicBottomNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox;
import com.pranavpandey.android.dynamic.support.widget.DynamicDrawerLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicEditText;
import com.pranavpandey.android.dynamic.support.widget.DynamicExtendedFloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicFloatingActionButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView;
import com.pranavpandey.android.dynamic.support.widget.DynamicListView;
import com.pranavpandey.android.dynamic.support.widget.DynamicMaterialCardView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView;
import com.pranavpandey.android.dynamic.support.widget.DynamicNestedScrollView;
import com.pranavpandey.android.dynamic.support.widget.DynamicPopupBackground;
import com.pranavpandey.android.dynamic.support.widget.DynamicProgressBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicRadioButton;
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView;
import com.pranavpandey.android.dynamic.support.widget.DynamicScrollView;
import com.pranavpandey.android.dynamic.support.widget.DynamicSeekBar;
import com.pranavpandey.android.dynamic.support.widget.DynamicSpinner;
import com.pranavpandey.android.dynamic.support.widget.DynamicSwipeRefreshLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicSwitchCompat;
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextInputEditText;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextInputLayout;
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView;
import com.pranavpandey.android.dynamic.support.widget.DynamicToolbar;
import com.pranavpandey.android.dynamic.support.widget.DynamicViewPager;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

/**
 * A layout inflater factory2 to replace original views with the dynamic support views
 * during inflation.
 */
public class DynamicLayoutInflater implements LayoutInflater.Factory2 {

    /**
     * Tag to ignore the view during inflation.
     */
    protected static final String ADS_TAG_IGNORE = ":ads_ignore";

    @Override
    public @Nullable View onCreateView(@NonNull String name,
            @NonNull Context context, @NonNull AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    @Override
    @SuppressLint("RestrictedApi")
    public @Nullable View onCreateView(@Nullable View parent, final @NonNull String name,
            final @NonNull Context context, final @NonNull AttributeSet attrs) {
        View view = null;

        switch (name) {
            case "ListView":
                view = new DynamicListView(context, attrs);
                break;
            case "ScrollView":
                view = new DynamicScrollView(context, attrs);
                break;
            case "android.support.v4.widget.DrawerLayout":
            case "androidx.DrawerLayout.widget.DrawerLayout":
                view = new DynamicDrawerLayout(context, attrs);
                break;
            case "android.support.v4.widget.SwipeRefreshLayout":
            case "androidx.SwipeRefreshLayout.widget.SwipeRefreshLayout":
                view = new DynamicSwipeRefreshLayout(context, attrs);
                break;
            case "Toolbar":
            case "android.support.v7.widget.Toolbar":
            case "androidx.appcompat.widget.Toolbar":
            case "com.google.android.material.appbar.MaterialToolbar":
                view = new DynamicToolbar(context, attrs);
                break;
            case "ListMenuItemView":
            case "android.support.v7.view.menu.ListMenuItemView":
            case "androidx.appcompat.view.menu.ListMenuItemView":
                try {
                    final View menuItemView = LayoutInflater.from(context)
                            .createView(name, null, attrs);
                    final DynamicCardView cardView = new DynamicPopupBackground(context, attrs);
                    final @ColorInt int backgroundColor = cardView.getColor();

                    menuItemView.post(new Runnable() {
                        @Override
                        public void run() {
                            @ColorInt int tintColor = DynamicTheme
                                    .getInstance().get().getTintBackgroundColor();
                            if (DynamicTheme.getInstance().get().isBackgroundAware()) {
                                tintColor = DynamicColorUtils.getContrastColor(
                                        tintColor, backgroundColor);
                            }

                            final Drawable icon = ((ListMenuItemView) menuItemView)
                                    .getItemData().getIcon();

                            if (icon != null) {
                                ((ListMenuItemView) menuItemView).setIcon(
                                        DynamicDrawableUtils.colorizeDrawable(icon, tintColor));
                            }

                            if (menuItemView.getParent() != null) {
                                ViewGroup menu = (ViewGroup) menuItemView.getParent();
                                ViewGroup group = (ViewGroup) menu.getParent();

                                DynamicScrollUtils.setEdgeEffectColor(
                                        (ListView) menu, tintColor);

                                if (DynamicSdkUtils.is21()) {
                                    if (!(group instanceof CardView)) {
                                        if (group.getBackground() != null) {
                                            if (group.getBackground()
                                                    instanceof GradientDrawable) {
                                                GradientDrawable background =
                                                        (GradientDrawable) group.getBackground();
                                                background.setCornerRadius(DynamicTheme
                                                        .getInstance().get().getCornerRadius());
                                            } else if (group.getBackground()
                                                    instanceof LayerDrawable) {
                                                GradientDrawable background = (GradientDrawable)
                                                        ((LayerDrawable) group.getBackground())
                                                                .getDrawable(0);
                                                background.setCornerRadius(DynamicTheme
                                                        .getInstance().get().getCornerRadius());
                                                ViewCompat.setBackground(group, background);
                                            }

                                            DynamicDrawableUtils.colorizeDrawable(
                                                    group.getBackground(), backgroundColor);
                                        }

                                        group.removeAllViews();
                                        group.addView(cardView);
                                        cardView.addView(menu);
                                    } else {
                                        group.removeAllViews();
                                        group.addView(menu);
                                    }
                                } else {
                                    ViewCompat.setBackground(menu, DynamicDrawableUtils
                                            .colorizeDrawable(DynamicResourceUtils.getDrawable(
                                                    context, R.drawable.ads_background),
                                                    backgroundColor));
                                }
                            }
                        }
                    });

                    view = menuItemView;
                } catch (Exception ignored) {
                }
                break;
            case "Button":
                view = new Button(context, attrs);
                ((Button) view).setTextColor(DynamicResourceUtils.getColorStateList(
                        DynamicTheme.getInstance().get().getTintBackgroundColor()));
                break;
            case "android.support.v7.widget.AppCompatButton":
            case "androidx.appcompat.widget.AppCompatButton":
            case "com.google.android.material.button.MaterialButton":
                view = new DynamicButton(context, attrs);
                break;
            case "ImageButton":
            case "android.support.v7.widget.AppCompatImageButton":
            case "androidx.appcompat.widget.AppCompatImageButton":
                view = new DynamicImageButton(context, attrs);
                break;
            case "ImageView":
            case "android.support.v7.widget.AppCompatImageView":
            case "androidx.appcompat.widget.AppCompatImageView":
                view = new DynamicImageView(context, attrs);
            break;
            case "TextView":
            case "android.support.v7.widget.AppCompatTextView":
            case "androidx.appcompat.widget.AppCompatTextView":
            case "com.google.android.material.textview.MaterialTextView":
                view = new DynamicTextView(context, attrs);
                break;
            case "CheckBox":
            case "android.support.v7.widget.AppCompatCheckBox":
            case "androidx.appcompat.widget.AppCompatCheckBox":
            case "com.google.android.material.checkbox.MaterialCheckBox":
                view = new DynamicCheckBox(context, attrs);
                break;
            case "RadioButton":
            case "android.support.v7.widget.AppCompatRadioButton":
            case "androidx.appcompat.widget.AppCompatRadioButton":
            case "com.google.android.material.radiobutton.MaterialRadioButton":
                view = new DynamicRadioButton(context, attrs);
                break;
            case "EditText":
            case "android.support.v7.widget.AppCompatEditText":
            case "androidx.appcompat.widget.AppCompatEditText":
                view = new DynamicEditText(context, attrs);
                break;
            case "android.support.v7.widget.SwitchCompat":
            case "androidx.appcompat.widget.SwitchCompat":
            case "com.google.android.material.switchmaterial.SwitchMaterial":
                view = new DynamicSwitchCompat(context, attrs);
                break;
            case "SeekBar":
            case "android.support.v7.widget.AppCompatSeekBar":
            case "androidx.appcompat.widget.AppCompatSeekBar":
                view = new DynamicSeekBar(context, attrs);
                break;
            case "Spinner":
            case "android.support.v7.widget.AppCompatSpinner":
            case "androidx.appcompat.widget.AppCompatSpinner":
                view = new DynamicSpinner(context, attrs);
                break;
            case "ProgressBar":
            case "android.support.v4.widget.ContentLoadingProgressBar":
            case "androidx.core.widget.ContentLoadingProgressBar":
                view = new DynamicProgressBar(context, attrs);
                break;
            case "android.support.v7.widget.RecyclerView":
            case "androidx.recyclerview.widget.RecyclerView":
                view = new DynamicRecyclerView(context, attrs);
                break;
            case "android.support.v4.widget.NestedScrollView":
            case "androidx.core.widget.NestedScrollView":
                view = new DynamicNestedScrollView(context, attrs);
                break;
            case "android.support.v4.view.ViewPager":
            case "androidx.viewpager.widget.ViewPager":
                view = new DynamicViewPager(context, attrs);
                break;
            case "android.support.design.bottomappbar.BottomAppBar":
            case "com.google.android.material.bottomappbar.BottomAppBar":
                view = new DynamicBottomAppBar(context, attrs);
                break;
            case "android.support.design.widget.NavigationView":
            case "com.google.android.material.navigation.NavigationView":
                view = new DynamicNavigationView(context, attrs);
                break;
            case "android.support.design.widget.BottomNavigationView":
            case "com.google.android.material.bottomnavigation.BottomNavigationView":
                view = new DynamicBottomNavigationView(context, attrs);
                break;
            case "android.support.design.widget.TabLayout":
            case "com.google.android.material.tabs.TabLayout":
                view = new DynamicTabLayout(context, attrs);
                break;
            case "CardView":
            case "android.support.v7.widget.CardView":
            case "androidx.cardview.widget.CardView":
                view = new DynamicCardView(context, attrs);
                break;
            case "com.google.android.material.card.MaterialCardView":
                view = new DynamicMaterialCardView(context, attrs);
                break;
            case "android.support.design.widget.TextInputLayout":
            case "com.google.android.material.textfield.TextInputLayout":
                view = new DynamicTextInputLayout(context, attrs);
                break;
            case "android.support.design.widget.TextInputEditText":
            case "com.google.android.material.textfield.TextInputEditText":
                view = new DynamicTextInputEditText(context, attrs);
                break;
            case "android.support.design.widget.FloatingActionButton":
            case "com.google.android.material.floatingactionbutton.FloatingActionButton":
                view = new DynamicFloatingActionButton(context, attrs);
                break;
            case "com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton":
                view = new DynamicExtendedFloatingActionButton(context, attrs);
                break;
        }

        if (view != null && ADS_TAG_IGNORE.equals(view.getTag())) {
            view = null;
        }

        return view;
    }
}
