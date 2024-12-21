package com.infolangues.mondico;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

/*
 * @class : ScrollView activable et désactivable (vérouillage sur sa position) via la méthode 'setScrollable'
 * (pour pouvoir vérouiller le scroll de la page principale quand la recherche avancée est active par dessus)
 */
public class ScrollViewModulable extends ScrollView {

        private boolean mScrollable = true;

        public ScrollViewModulable(Context context) {
            super(context);
        }

        public ScrollViewModulable(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ScrollViewModulable(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ScrollViewModulable(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        /*
         * @function : active ou désactive (vérouille sur sa position) la ScrollViewModulable
         */
        public void setScrollable(boolean enabled) {
            mScrollable = enabled;
        }

        public boolean isScrollable() {
            return mScrollable;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {//laisse la gestion de l'évènement à la superClasse quand le ScrollViewModulable est actif (mScrollable = true), sinon ne fait rien
            return mScrollable && super.onTouchEvent(ev);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {//laisse la gestion de l'évènement à la superClasse quand le ScrollViewModulable est actif (mScrollable = true), sinon ne fait rien
            return mScrollable && super.onInterceptTouchEvent(ev);
        }
}
