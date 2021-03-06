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

package com.pranavpandey.android.dynamic.support.utils;

import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.widget.EdgeEffectCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.lang.reflect.Field;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Helper class to set {@link EdgeEffect} or glow color and scroll bar color for the supported 
 * views dynamically by using reflection. It will be used to match the color with the app's theme.
 */
@RestrictTo(LIBRARY_GROUP)
public final class DynamicScrollUtils {

    /**
     * {@link EdgeEffect} field constant for the edge.
     */
    private static Field EDGE_EFFECT_FIELD_EDGE;
    /**
     * {@link EdgeEffect} field constant for the glow.
     */
    private static Field EDGE_EFFECT_FIELD_GLOW;
    /**
     * {@link EdgeEffectCompat} field constant for the edge effect.
     */
    private static Field EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT;

    /**
     * {@link AbsListView} field constant for the top glow.
     */
    private static Field LIST_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link AbsListView} field constant for the bottom glow.
     */
    private static Field LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM;
    /**
     * {@link RecyclerView} field constant for the top glow.
     */
    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link RecyclerView} field constant for the left glow.
     */
    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT;
    /**
     * {@link RecyclerView} field constant for the right glow.
     */
    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT;
    /**
     * {@link RecyclerView} field constant for the bottom glow.
     */
    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    /**
     * {@link ScrollView} field constant for the top glow.
     */
    private static Field SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link ScrollView} field constant for the bottom glow.
     */
    private static Field SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;
    /**
     * {@link NestedScrollView} field constant for the top glow.
     */
    private static Field NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    /**
     * {@link NestedScrollView} field constant for the bottom glow.
     */
    private static Field NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    /**
     * {@link ViewPager} field constant for the left glow.
     */
    private static Field VIEW_PAGER_FIELD_EDGE_GLOW_LEFT;
    /**
     * {@link ViewPager} field constant for the right glow.
     */
    private static Field VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT;

    /**
     * {@link NavigationView} field constant for the presenter.
     */
    private static Field NAVIGATION_VIEW_FIELD_PRESENTER;
    /**
     * {@link NavigationView} field constant for the recycler view.
     */
    private static Field NAVIGATION_VIEW_FIELD_RECYCLER_VIEW;

    /**
     * Scroll bar field constant for the view.
     */
    private static Field VIEW_SCROLL_BAR_FIELD;

    /**
     * Scroll bar cache constant for the view.
     */
    private static Field VIEW_SCROLL_BAR_FIELD_CACHE;

    /**
     * Scroll bar vertical thumb constant for the view.
     */
    private static Field VIEW_SCROLL_BAR_VERTICAL_THUMB;

    /**
     * Scroll bar horizontal thumb constant for the view.
     */
    private static Field VIEW_SCROLL_BAR_HORIZONTAL_THUMB;

    /**
     * Initialize edge effect or glow fields so that we can access them via reflection.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void initializeEdgeEffectFields() {
        if (EDGE_EFFECT_FIELD_EDGE != null
                && EDGE_EFFECT_FIELD_GLOW != null
                && EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT != null) {
            EDGE_EFFECT_FIELD_EDGE.setAccessible(true);
            EDGE_EFFECT_FIELD_GLOW.setAccessible(true);
            EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true);

            return;
        }

        if (DynamicSdkUtils.is21()) {
            EDGE_EFFECT_FIELD_EDGE = null;
            EDGE_EFFECT_FIELD_GLOW = null;
        } else if (DynamicSdkUtils.is14()) {
            Field edge = null;
            Field glow = null;
            for (Field field : EdgeEffect.class.getDeclaredFields()) {
                switch (field.getName()) {
                    case "mEdge":
                        field.setAccessible(true);
                        edge = field;
                        break;
                    case "mGlow":
                        field.setAccessible(true);
                        glow = field;
                        break;
                }
            }

            EDGE_EFFECT_FIELD_EDGE = edge;
            EDGE_EFFECT_FIELD_GLOW = glow;
        }

        Field edgeEffectCompat = null;
        try {
            edgeEffectCompat = EdgeEffectCompat.class.getDeclaredField("mEdgeEffect");
        } catch (NoSuchFieldException ignored) {
        }
        EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT = edgeEffectCompat;
    }

    /**
     * Initialize recycler view fields so that we can access them via reflection.
     */
    private static void initializeRecyclerViewFields() {
        if (RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP != null
                && RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT != null
                && RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT != null
                && RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.setAccessible(true);
            RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.setAccessible(true);
            RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        Class<?> clazz = RecyclerView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mTopGlow":
                    field.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mBottomGlow":
                    field.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
                case "mLeftGlow":
                    field.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT = field;
                    break;
                case "mRightGlow":
                    field.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT = field;
                    break;
            }
        }
    }

    /**
     * Initialize abs list view fields so that we can access them via reflection.
     */
    private static void initializeListViewFields() {
        if (LIST_VIEW_FIELD_EDGE_GLOW_TOP != null
                && LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            LIST_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        final Class<?> clazz = AbsListView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    LIST_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize scroll view fields so that we can access them via reflection.
     */
    private static void initializeScrollViewFields() {
        if (SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null
                && SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        final Class<?> clazz = ScrollView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize nested scroll view fields so that we can access them via reflection.
     */
    private static void initializeNestedScrollViewFields() {
        if (NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null
                && NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);

            return;
        }

        Class<?> clazz = NestedScrollView.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mEdgeGlowTop":
                    field.setAccessible(true);
                    NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = field;
                    break;
                case "mEdgeGlowBottom":
                    field.setAccessible(true);
                    NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = field;
                    break;
            }
        }
    }

    /**
     * Initialize view pager fields so that we can access them via reflection.
     */
    private static void initializeViewPagerFields() {
        if (VIEW_PAGER_FIELD_EDGE_GLOW_LEFT != null
                && VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT != null) {
            VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.setAccessible(true);
            VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.setAccessible(true);

            return;
        }

        Class<?> clazz = ViewPager.class;
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getName()) {
                case "mLeftEdge":
                    field.setAccessible(true);
                    VIEW_PAGER_FIELD_EDGE_GLOW_LEFT = field;
                    break;
                case "mRightEdge":
                    field.setAccessible(true);
                    VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT = field;
                    break;
            }
        }
    }

    /**
     * Initialize navigation view fields so that we can access them via reflection.
     */
    private static void initializeNavigationViewFields() {
        if (NAVIGATION_VIEW_FIELD_PRESENTER != null
                && NAVIGATION_VIEW_FIELD_RECYCLER_VIEW != null) {
            NAVIGATION_VIEW_FIELD_PRESENTER.setAccessible(true);
            NAVIGATION_VIEW_FIELD_RECYCLER_VIEW.setAccessible(true);

            return;
        }

        Class<?> clazz = NavigationView.class;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("presenter")) {
                field.setAccessible(true);
                NAVIGATION_VIEW_FIELD_PRESENTER = field;

                break;
            }
        }

        Class<?> clazz1 = NavigationMenuPresenter.class;
        for (Field field : clazz1.getDeclaredFields()) {
            if (field.getName().equals("menuView")) {
                field.setAccessible(true);
                NAVIGATION_VIEW_FIELD_RECYCLER_VIEW = field;

                break;
            }
        }
    }

    /**
     * Initialize scroll bar fields so that we can access them via reflection.
     */
    private static void initializeScrollBarFields(@NonNull View view) {
        try {
            if (VIEW_SCROLL_BAR_FIELD_CACHE == null) {
                VIEW_SCROLL_BAR_FIELD_CACHE = View.class.getDeclaredField("mScrollCache");
                VIEW_SCROLL_BAR_FIELD_CACHE.setAccessible(true);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for list view.
     *
     * @param listView The list view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull AbsListView listView, @ColorInt int color) {
        initializeListViewFields();

        try {
            Object edgeEffect = LIST_VIEW_FIELD_EDGE_GLOW_TOP.get(listView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(listView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for recycler view.
     *
     * @param recyclerView The recycler view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull RecyclerView recyclerView, @ColorInt int color) {
        initializeRecyclerViewFields();

        try {
            Object edgeEffect = RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.get(recyclerView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for recycler view.
     *
     * @param recyclerView The recycler view to set the edge effect color.
     * @param color The edge effect color to be set.
     * @param scrollListener Scroll listener to set color on over scroll.
     */
    public static void setEdgeEffectColor(@Nullable RecyclerView recyclerView,
            final @ColorInt int color, @Nullable RecyclerView.OnScrollListener scrollListener) {
        if (recyclerView == null) {
            return;
        }

        if (scrollListener == null) {
            scrollListener =
                    new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView,
                                int newState) {
                            super.onScrollStateChanged(recyclerView, newState);

                            setEdgeEffectColor(recyclerView, color);
                        }
                    };

            recyclerView.removeOnScrollListener(scrollListener);
            recyclerView.addOnScrollListener(scrollListener);
        }

        setEdgeEffectColor(recyclerView, color);
    }

    /**
     * Set edge effect or glow color for scroll view.
     *
     * @param scrollView The scroll view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull ScrollView scrollView, @ColorInt int color) {
        initializeScrollViewFields();

        try {
            Object edgeEffect = SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for nested scroll view.
     *
     * @param nestedScrollView The nested scroll view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(
            @NonNull NestedScrollView nestedScrollView, @ColorInt int color) {
        initializeNestedScrollViewFields();

        try {
            Object edgeEffect =
                    NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(nestedScrollView);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect =
                    NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(nestedScrollView);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for view pager.
     *
     * @param viewPager The view pager to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(@NonNull ViewPager viewPager, @ColorInt int color) {
        initializeViewPagerFields();

        try {
            Object edgeEffect = VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.get(viewPager);
            setEdgeEffectColor(edgeEffect, color);
            edgeEffect = VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.get(viewPager);
            setEdgeEffectColor(edgeEffect, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set edge effect or glow color for navigation view.
     *
     * @param navigationView The navigation view to set the edge effect color.
     * @param color The edge effect color to be set.
     */
    public static void setEdgeEffectColor(
            @NonNull NavigationView navigationView, @ColorInt int color) {
        initializeNavigationViewFields();

        try {
            NavigationMenuPresenter presenter = (NavigationMenuPresenter)
                    NAVIGATION_VIEW_FIELD_PRESENTER.get(navigationView);
            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    NAVIGATION_VIEW_FIELD_RECYCLER_VIEW.get(presenter);
            setEdgeEffectColor(navigationMenuView, color, null);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set color of the supplied edge effect object.
     *
     * @param edgeEffect The edge effect object to set the color.
     * @param color The edge effect color to be set.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEdgeEffectColor(@Nullable Object edgeEffect, @ColorInt int color) {
        initializeEdgeEffectFields();

        if (edgeEffect instanceof EdgeEffectCompat) {
            try {
                EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true);
                edgeEffect = EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.get(edgeEffect);
            } catch (IllegalAccessException illegalAccessException) {
                return;
            }
        }

        if (edgeEffect == null) {
            return;
        }

        if (DynamicSdkUtils.is21()) {
            ((EdgeEffect) edgeEffect).setColor(color);
        } else {
            try {
                EDGE_EFFECT_FIELD_EDGE.setAccessible(true);
                final Drawable mEdge = (Drawable) EDGE_EFFECT_FIELD_EDGE.get(edgeEffect);
                EDGE_EFFECT_FIELD_GLOW.setAccessible(true);
                final Drawable mGlow = (Drawable) EDGE_EFFECT_FIELD_GLOW.get(edgeEffect);
                if (mGlow != null) {
                    mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    mGlow.setCallback(null);
                }

                if (mEdge != null) {
                    mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    mEdge.setCallback(null);
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Set scroll bar color for navigation view.
     *
     * @param navigationView The navigation view to set the scroll bar color.
     * @param color The edge effect color to be set.
     */
    public static void setScrollBarColor(
            @NonNull NavigationView navigationView, @ColorInt int color) {
        initializeNavigationViewFields();

        try {
            NavigationMenuPresenter presenter = (NavigationMenuPresenter)
                    NAVIGATION_VIEW_FIELD_PRESENTER.get(navigationView);
            NavigationMenuView navigationMenuView = (NavigationMenuView)
                    NAVIGATION_VIEW_FIELD_RECYCLER_VIEW.get(presenter);
            setScrollBarColor(navigationMenuView, color);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set scroll bar color for view.
     *
     * @param view The view to set the scroll bar color.
     * @param color The scroll bar color.
     */
    public static void setScrollBarColor(@Nullable View view, @ColorInt int color) {
        if (view == null) {
            return;
        }

        initializeScrollBarFields(view);
        color = DynamicColorUtils.getLessVisibleColor(color);

        if (VIEW_SCROLL_BAR_FIELD_CACHE == null) {
            return;
        }

        try {
            Object mScrollCache = VIEW_SCROLL_BAR_FIELD_CACHE.get(view);

            if (mScrollCache != null) {
                VIEW_SCROLL_BAR_FIELD =
                        mScrollCache.getClass().getDeclaredField("scrollBar");
                VIEW_SCROLL_BAR_FIELD.setAccessible(true);
                Object scrollBar = VIEW_SCROLL_BAR_FIELD.get(mScrollCache);

                if (scrollBar != null) {
                    VIEW_SCROLL_BAR_VERTICAL_THUMB =
                            scrollBar.getClass().getDeclaredField("mVerticalThumb");
                    VIEW_SCROLL_BAR_VERTICAL_THUMB.setAccessible(true);

                    if (VIEW_SCROLL_BAR_VERTICAL_THUMB != null) {
                        DynamicDrawableUtils.colorizeDrawable((Drawable)
                                VIEW_SCROLL_BAR_VERTICAL_THUMB.get(scrollBar), color);
                    }
                }
            }

            // Fix for Android 9 developer preview. For more info, please
            // visit g.co/dev/appcompat.
//            VIEW_SCROLL_BAR_HORIZONTAL_THUMB =
//                    scrollBar.getClass().getDeclaredField("mHorizontalThumb");
//            VIEW_SCROLL_BAR_HORIZONTAL_THUMB.setAccessible(true);
//            if (VIEW_SCROLL_BAR_HORIZONTAL_THUMB != null) {
//                DynamicDrawableUtils.colorizeDrawable((Drawable)
//                        VIEW_SCROLL_BAR_HORIZONTAL_THUMB.get(scrollBar), color);
//            }
        } catch(Exception ignored) {
        }
    }
}
