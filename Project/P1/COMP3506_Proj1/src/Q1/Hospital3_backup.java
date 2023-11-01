package Q1;

import java.util.Iterator;
import java.util.Objects;

public class Hospital3_backup extends HospitalBase {

    public int appts_load;     // Number of appts stored in the array. Keeps track of index.

    public PatientBase[] appointments;

    public Hospital3_backup() {
        /* Add your code here! */
        super();
        this.appointments = new PatientBase[1];
        this.appts_load = 0;
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        // Add patients to the end of the array. Double array in size if full

        // Check if patient is within hospital hours
        if (!((Patient) patient).inHospitalHours()) {
            // Patient is trying to book at an invalid time
            return false;
        }

        if (this.appts_load + 1 >= this.appointments.length) {
            // Need to double the size of the array
            int new_length = this.appointments.length * 2;
            PatientBase[] new_appointments = new PatientBase[new_length];
            // Copy existing appointments across
            for (int i = 0; i < this.appointments.length; i++) {
                new_appointments[i] = this.appointments[i];
            }
            // Set appointments to new_appointments
            this.appointments = new_appointments;
        }

        this.appointments[this.appts_load] = patient;
        this.appts_load += 1;
        return true;
//
//
//        // Idea is to have buckets - one created per new time.
//        // Also have a list of these buckets (unsorted)
//
//
//        if (pt_hours >= 13) {
//            pt_hours--;
//        }
//
//        int pt_time = pt_hours * 60 + pt_minutes;
    }

    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */
        // As array isn't ordered (to achieve the O(1) insert time complexity), we must
        // first sort the array, before returning an iterator

        // Create new array of the same size
//        PatientBase[] sorted_appts = new PatientBase[this.appointments.length];

        // Merge Sort Algorithm
//        int end = this.appointments.length;
//        MergeSort ms = new MergeSort();
//        ms.sort(this.appointments, 0, end - 1);
        // Insertion sort
        int n = this.appointments.length;
        for (int i = 1; i < this.appts_load; ++i) {
            PatientBase key = this.appointments[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && this.appointments[j].compareTo(key) > 0) {
                this.appointments[j + 1] = this.appointments[j];
                j = j - 1;
            }
            this.appointments[j + 1] = key;
        }

        // Make iterator

        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < n && appointments[currentIndex] != null;
            }

            @Override
            public PatientBase next() {
                return appointments[currentIndex++];
            }
        };
    }

    /* Add any extra functions below */

    public static void main(String[] args) {
        /*//*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * The following main method is provided for simple debugging only
         */
        var hospital = new Hospital3_backup();
        var p1 = new Patient("Max", "11:00");
        var p2 = new Patient("Alex", "14:00");
        var p3 = new Patient("George", "14:00");
        hospital.addPatient(p1);
        hospital.addPatient(p2);
        hospital.addPatient(p3);


        var patients = new Patient[] {p1, p2, p3};
        int i = 0;
        for (var patient : hospital) {
            assert Objects.equals(patient, patients[i++]);
        }
        System.out.println("Iterator time");
        Iterator<PatientBase> it = hospital.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
//        System.out.println("Stop");

    }
}
