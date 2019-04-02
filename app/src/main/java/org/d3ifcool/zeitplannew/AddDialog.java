package org.d3ifcool.zeitplannew;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddDialog extends AppCompatDialogFragment {
    private Spinner spinnerHari;
    private EditText editTextMatakuliah, editTextKelas, editTextDosen, editTextRuangan;
    TimePicker timePicker;
//    private AddDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add, null);

        builder.setView(view)
                .setTitle("Tambah Jadwal")
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String hari = spinnerHari.getSelectedItem().toString();
                        String matakuliah = editTextMatakuliah.getText().toString();
                        String kelas = editTextKelas.getText().toString();
                        String dosen = editTextDosen.getText().toString();
                        String ruangan = editTextRuangan.getText().toString();
                        int hour, minute;
                        if (Build.VERSION.SDK_INT >= 23) {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        } else {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        }
                        String hourString = null;
                        String nol = "0";
                        String minuteString = String.valueOf(minute);
                        String minuteStringFix = null;
                        if (hour < 10) {
                            hourString = nol + String.valueOf(hour);
                        }else{
                            hourString = String.valueOf(hour);
                        }
                        if (minute < 10){
                            minuteStringFix = nol.concat(minuteString);
                        }else{
                            minuteStringFix = minuteString;
                        }
                        String waktu = hourString + ":" + minuteStringFix;

//                        listener.insertDB(hari,matakuliah,kelas,dosen,ruangan,waktu);

                        Calendar calendar = Calendar.getInstance();

                        if (Build.VERSION.SDK_INT >= 23) {
                            calendar.set(
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH),
                                    timePicker.getHour(),
                                    timePicker.getMinute(),
                                    0
                            );
                        } else {
                            calendar.set(
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH),
                                    timePicker.getCurrentHour(),
                                    timePicker.getCurrentMinute(),
                                    0
                            );
                        }
//                        setReminder(calendar.getTimeInMillis());
                    }
                });

        spinnerHari = view.findViewById(R.id.spinnerHari);
        editTextMatakuliah = view.findViewById(R.id.editText_matakuliah);
        editTextKelas = view.findViewById(R.id.editText_kelas);
        editTextDosen = view.findViewById(R.id.editText_dosen);
        editTextRuangan = view.findViewById(R.id.editText_ruangan);
        timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        return builder.create();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (AddDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()+ "must implement AddDialogListener");
//        }
//
//    }
//
//    public interface AddDialogListener{
//        void insertDB(String hari, String matakuliah, String kelas, String dosen, String ruangan, String waktu);
//    }
}
