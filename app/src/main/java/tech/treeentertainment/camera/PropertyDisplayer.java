
package tech.treeentertainment.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import tech.treeentertainment.camera.ptp.Camera;
import tech.treeentertainment.camera.util.DimenUtil;

public class PropertyDisplayer {

    private final PropertyToggle toggleButton;
    private final LinearLayout listContainer;
    private final ListView list;
    private final int property;
    private int[] values = new int[0];
    private int value;
    private final Context context;
    private Camera camera;
    private int biggestValueWidth = -1;
    private PropertyAdapter<?> adapter;
    private int checkboxWidth = -1;
    private boolean dataChanged;
    private final CheckBox autoHideCb;
    private boolean editable;

    public PropertyDisplayer(Context context, View container, LayoutInflater inflater, int property, int toggleBtnId,
            String title) {
        this.context = context;
        this.property = property;
        this.toggleButton = (PropertyToggle) container.findViewById(toggleBtnId);
        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggled();
            }
        });

        // there seems to be a layouting bug when setting this to "", so initialize it at least to " "
        toggleButton.setText(" ");
        toggleButton.setIcon(null);

        listContainer = (LinearLayout) inflater.inflate(R.layout.property_listview, null);
        ((TextView) listContainer.findViewById(R.id.title)).setText(title);
        list = (ListView) listContainer.findViewById(android.R.id.list);
        listContainer.setVisibility(View.GONE);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                camera.setProperty(PropertyDisplayer.this.property, values[position]);
                if (autoHideCb.isChecked()) {
                    toggleButton.setChecked(false);
                    onToggled();
                }
            }
        });
        listContainer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));

        autoHideCb = (CheckBox) listContainer.findViewById(R.id.autoHideCB);

        CheckBox cb = new CheckBox(context);
        cb.setText("Hide");
        cb.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        checkboxWidth = cb.getMeasuredWidth();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public PropertyToggle getToggle() {
        return toggleButton;
    }

    public LinearLayout getList() {
        return listContainer;
    }

    public void setPropertyDesc(int[] values, String[] valuesStrings, Integer[] icons) {
        this.values = values;
        toggleButton.setEnabled(editable && values.length > 0);

        if (icons != null) {
            Drawable drawables[] = new Drawable[icons.length];
            for (int i = 0; i < icons.length; ++i) {
                if (icons[i] == null) {
                    continue;
                }
                Drawable d = context.getResources().getDrawable(icons[i]);
                drawables[i] = d;
                if (biggestValueWidth == -1) {
                    biggestValueWidth = d.getIntrinsicWidth();
                    layoutListView(biggestValueWidth);
                }
            }
            adapter = new PropertyAdapter<Drawable>(context, R.layout.property_icon_list_item, drawables);
        } else {
            String longest = "";
            int longestLength = 0;
            for (String s : valuesStrings) {
                int len = s.length();
                if (len > longestLength) {
                    longest = s;
                    longestLength = len;
                }
            }
            TextView t = new TextView(context);
            // HACK HACK
            if (context.getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
                t.setTextAppearance(context, android.R.style.TextAppearance_Large);
            } else {
                t.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            }
            t.setText(longest + " ");
            t.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            biggestValueWidth = t.getMeasuredWidth();
            layoutListView(biggestValueWidth);
            adapter = new PropertyAdapter<String>(context, R.layout.property_list_item, valuesStrings);
        }
        list.setAdapter(adapter);

        if (toggleButton.isChecked()) {
            if (values.length == 0) {
                listContainer.setVisibility(View.GONE);
            } else {
                listContainer.setVisibility(View.VISIBLE);
                updateCurrentPosition();
            }
        } else {
            dataChanged = true;
        }
    }

    public void setProperty(int value, String text, Integer iconId) {
        this.value = value;
        if (iconId != null) {
            toggleButton.setText(""); // 텍스트 제거
            Drawable d = context.getResources().getDrawable(iconId);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            toggleButton.setIcon(d); // 아이콘 표시
        } else {
            toggleButton.setIcon(null); // 아이콘 제거
            toggleButton.setText(text); // 텍스트 표시
        }
        toggleButton.setChecked(toggleButton.isChecked());

        if (toggleButton.isChecked()) {
            updateCurrentPosition();
        } else {
            dataChanged = true;
        }
    }



    private void updateCurrentPosition() {
        for (int i = 0; i < values.length; ++i) {
            if (value == values[i]) {
                if (adapter != null) {
                    adapter.setCurrentPosition(i);
                    if (listContainer.getVisibility() == View.GONE) {
                        list.setSelectionFromTop(i - 3, 0);
                    }
                }
                break;
            }
        }
    }

    public void onToggled() {
        if (toggleButton.isChecked()) {
            if (dataChanged) {
                dataChanged = false;
                updateCurrentPosition();
            }
            listContainer.setVisibility(View.VISIBLE);
        } else {
            listContainer.setVisibility(View.GONE);
        }
    }

    private void layoutListView(int itemWidth) {
        int paddingRight = (int) DimenUtil.dpToPx(context, 8);
        int listWidth = itemWidth + paddingRight + list.getListPaddingLeft() + list.getListPaddingRight()
                + list.getVerticalScrollbarWidth();
        listContainer.getLayoutParams().width = Math.max(checkboxWidth, listWidth);
        listContainer.requestLayout();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        toggleButton.setEnabled(editable && values.length > 0);
        if (!editable && toggleButton.isChecked()) {
            toggleButton.setChecked(false);
            onToggled();
        }
    }

    public void setAutoHide(boolean hide) {
        autoHideCb.setChecked(hide);
    }

    public boolean getAutoHide() {
        return autoHideCb.isChecked();
    }
}
