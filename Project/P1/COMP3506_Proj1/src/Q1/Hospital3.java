//package Q1;

import java.util.Iterator;

public class Hospital3 extends HospitalBase {

    /* Number of appts stored in the array. Keeps track of index. */
    public int appts_load;

    Appointment head = null;
    Appointment tail = null;

    /* Helper class to handle the linked list */
    static class Appointment {
        /* Stores patient at this appointment slot */
        public PatientBase patient;

        /* Pointer to next appointment in the linked list */
        public Appointment next;

        public Appointment(PatientBase patient) {
            this.patient = patient;
        }
    }

    public Hospital3() {
        super();
        this.appts_load = 0;
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /**
         * Adds patinet to the end of the linked list. Linked list remains in an unsorted state until the iterator
         * function is called. Duplicate patients are handled by first in best dressed priority.
         * @param patient a new patient to be inserted into the array.
         * @returns False if patient is booking at an invalid time. True otherwise.
         */

        // Check if patient is within hospital hours
        if (!((Patient) patient).inHospitalHours()) {
            return false;   // Patient is trying to book at an invalid time
        }

        Appointment newAppt = new Appointment(patient);

        // base case if no elements
        if (this.head == null && this.tail == null) {
            this.head = newAppt;
            this.tail = newAppt;
        }

        // make tail this appt and make prevTail newAppt
        this.tail.next = newAppt;
        this.tail = newAppt;

        return true;
    }

    @Override
    public Iterator<PatientBase> iterator() {
        /**
         * Iterates over patient appointments in the array. Array isn't yet sorted, so we use merge sort before getting
         * the next patient.
         */

        Appointment sortedHead = this.mergeSort(this.head);

        return new Iterator<>() {

            private Appointment currentAppt = sortedHead;

            @Override
            public boolean hasNext() {
                return currentAppt != null;
            }

            @Override
            public PatientBase next() {
                PatientBase cur = currentAppt.patient;
                currentAppt = currentAppt.next;
                return cur;
            }
        };
    }

    Appointment sortedMerge(Appointment a, Appointment b) {
        /**
         * Merges two linked lists together into one and returns the head of the newly merged linked list.
         * Derived from https://www.geeksforgeeks.org/merge-sort-for-linked-list/
         * @param a: first sublist to be merged
         * @param b: second sublist to be merged
         * @returns pointer to the head of the newly merged linked list
         */

        Appointment result;     // pointer used for head of new linked-list.

        // Base cases
        if (a == null)
            return b;
        if (b == null)
            return a;

        // Set head to the first appointment then recursively set the next pointer in asending order of time.
        if (a.patient.compareTo(b.patient) <= 0) {
            result = a;
            result.next = sortedMerge(a.next, b);
        } else {
            result = b;
            result.next = sortedMerge(a, b.next);
        }

        return result;      // return pointer to head of the linked list
    }

    Appointment mergeSort(Appointment h) {
        /**
         * Splits a linked list (with head h) recursively in half then calls sortedMerge to piece the linked list
         * back together in sorted order.
         * Derived from https://www.geeksforgeeks.org/merge-sort-for-linked-list/
         * @param h: pointer to head of linked list to be sorted
         * @returns pointer to head of the same linked list but sorted by appointment time
         */

        // Base case: if head is null
        if (h == null || h.next == null) {
            return h;
        }

        Appointment middle = getMiddle(h);              // Get the middle of the list
        Appointment nextOfMiddle = middle.next;         // Element directly following the middle element

        // Set the next of middle node to null so we know when to stop the next branch down the tree.
        middle.next = null;

        Appointment left = mergeSort(h);                // Apply mergeSort on left list
        Appointment right = mergeSort(nextOfMiddle);    // Apply mergeSort on right list

        return sortedMerge(left, right);                // Merge the left and right lists
    }

    public static Appointment getMiddle(Appointment head) {
        /**
         * Helper function to get the middle of the linked list. Works by using two pointers, half and full. Full
         * traverses linked list double the speed of half. When full is equal to null, half is then at the halfway point
         * of the linked list.
         * Derived from https://www.geeksforgeeks.org/merge-sort-for-linked-list/
         * @param head pointer to the head of the linekd list to find the middle of
         * @returns pointer to the middle of the same linked list
         */

        if (head == null) {
            return head;
        }

        Appointment half = head, full = head;

        while (full.next != null && full.next.next != null) {
            half = half.next;
            full = full.next.next;
        }
        return half;
    }

//    public static void main(String[] args) {
//        /*
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * The following main method is provided for simple debugging only
//         */
//        var hospital = new Hospital3();
//        var p1 = new Patient("Max", "11:00");
//        var p2 = new Patient("Alex", "14:00");
//        var p3 = new Patient("George", "14:00");
//        hospital.addPatient(p1);
//        hospital.addPatient(p2);
//        hospital.addPatient(p3);
//
//        var patients = new Patient[] {p1, p2, p3};
//        int i = 0;
//        for (var patient : hospital) {
//            System.out.println(patient);
//            assert Objects.equals(patient, patients[i++]);
//        }
//        System.out.println("\nIterator time");
//        Iterator<PatientBase> it = hospital.iterator();
//
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }
//    }
}
