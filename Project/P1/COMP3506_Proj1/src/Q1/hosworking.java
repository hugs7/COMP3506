package Q1;

import java.util.Iterator;

public class Hospital1 extends HospitalBase {
    public int appts_load;     // Number of appts stored in the array. Keeps track of index.
    public PatientBase[] appointments;

    public Hospital1() {
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
        if (!((Patient) patient).inStrictHospitalHours()) {
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

        // if empty array - base case
        if (this.appts_load == 0) {
            this.appointments[0] = patient;
            this.appts_load++;
            return true;
        }

        // loop through array and determine spot to place new element
        int j = 0;      // Base case for inserting at start of array

        // loop down method
        for (int i = this.appts_load - 1; i >= 0; i--) {
            int comp = patient.compareTo(this.appointments[i]);
            if (comp == 0) {
                // Duplicate not allowed
                return false;
            } else if (comp > 0) {
                j = i + 1;
                break;
            }
        }

        if (j >= this.appts_load) { // if no more elements in array
            this.appointments[j] = patient;
            this.appts_load++;
            return true;
        }
        // Else shift future appts up one index
        PatientBase pt = this.appointments[j];
        PatientBase nextPt;
        // insert patient at appointments[j]
        this.appointments[j] = patient;

        // Move any future elements forward one
        for (int i = j + 1; i <= this.appts_load; i++) {
            // looping over future appts
            nextPt = this.appointments[i];
            this.appointments[i] = pt;
            pt = nextPt;
        }

        this.appts_load++;
        return true;

    }

    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */

        // Make iterator

        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                int n = appointments.length;
                return currentIndex < n && appointments[currentIndex] != null;
            }

            @Override
            public PatientBase next() {
                return appointments[currentIndex++];
            }
        };
    }
}


