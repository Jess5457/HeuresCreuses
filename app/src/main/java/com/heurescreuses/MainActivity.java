package com.heurescreuses;

import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<PlageHoraire> plages = new ArrayList<>();
    private LinearLayout plagesContainer;
    private Runnable refreshRunnable;
    private final android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plagesContainer = findViewById(R.id.plages_container);
        plages = PrefsManager.loadPlages(this);

        renderPlages();
        updateStatus();

        // Refresh status every second
        refreshRunnable = new Runnable() {
            @Override public void run() {
                updateStatus();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(refreshRunnable);

        findViewById(R.id.btn_add).setOnClickListener(v -> {
            plages.add(new PlageHoraire(22, 0, 6, 0));
            renderPlages();
        });

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            PrefsManager.savePlages(this, plages);
            // Trigger widget update
            AppWidgetManager mgr = AppWidgetManager.getInstance(this);
            int[] ids = mgr.getAppWidgetIds(new ComponentName(this, HeuresCreusesWidget.class));
            for (int id : ids) HeuresCreusesWidget.updateAppWidget(this, mgr, id);
            Toast.makeText(this, "✅ Enregistré !", Toast.LENGTH_SHORT).show();
            updateStatus();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshRunnable);
    }

    private void updateStatus() {
        boolean creuse = PrefsManager.isCreuse(this);
        int next = PrefsManager.nextChange(this);

        TextView icon = findViewById(R.id.status_icon);
        TextView label = findViewById(R.id.status_label);
        TextView title = findViewById(R.id.status_title);
        TextView nextView = findViewById(R.id.status_next);

        icon.setText(creuse ? "🌙" : "☀️");

        if (creuse) {
            label.setText("TARIF RÉDUIT ACTIF");
            label.setTextColor(Color.parseColor("#00E5A0"));
            title.setText("HEURE CREUSE");
            title.setTextColor(Color.parseColor("#00E5A0"));
        } else {
            label.setText("TARIF PLEIN ACTIF");
            label.setTextColor(Color.parseColor("#FF4D6D"));
            title.setText("HEURE PLEINE");
            title.setTextColor(Color.parseColor("#FF4D6D"));
        }

        Calendar cal = Calendar.getInstance();
        int nowMin = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        int nowSec = cal.get(Calendar.SECOND);

        if (next >= 0) {
            int diff = ((next - nowMin) * 60 - nowSec + 86400) % 86400;
            int dh = diff / 3600;
            int dm = (diff % 3600) / 60;
            int ds = diff % 60;
            String prefix = creuse ? "Fin dans : " : "Début dans : ";
            nextView.setText(String.format("%s%02dh%02dm%02ds", prefix, dh, dm, ds));
        } else {
            nextView.setText("Aucune plage configurée");
        }
    }

    private void renderPlages() {
        plagesContainer.removeAllViews();
        for (int i = 0; i < plages.size(); i++) {
            final int idx = i;
            PlageHoraire p = plages.get(i);

            View item = LayoutInflater.from(this).inflate(R.layout.item_plage, plagesContainer, false);
            Button btnStart = item.findViewById(R.id.btn_start);
            Button btnEnd = item.findViewById(R.id.btn_end);
            Button btnDelete = item.findViewById(R.id.btn_delete);

            btnStart.setText(p.startStr());
            btnEnd.setText(p.endStr());

            btnStart.setOnClickListener(v -> {
                new TimePickerDialog(this, (tp, h, m) -> {
                    plages.get(idx).startHour = h;
                    plages.get(idx).startMin = m;
                    btnStart.setText(plages.get(idx).startStr());
                }, p.startHour, p.startMin, true).show();
            });

            btnEnd.setOnClickListener(v -> {
                new TimePickerDialog(this, (tp, h, m) -> {
                    plages.get(idx).endHour = h;
                    plages.get(idx).endMin = m;
                    btnEnd.setText(plages.get(idx).endStr());
                }, p.endHour, p.endMin, true).show();
            });

            btnDelete.setOnClickListener(v -> {
                plages.remove(idx);
                renderPlages();
            });

            plagesContainer.addView(item);
        }
    }
}
