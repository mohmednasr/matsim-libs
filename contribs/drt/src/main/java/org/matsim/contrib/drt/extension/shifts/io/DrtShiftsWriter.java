package org.matsim.contrib.drt.extension.shifts.io;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.drt.extension.shifts.shift.DrtShift;
import org.matsim.contrib.drt.extension.shifts.shift.DrtShifts;
import org.matsim.contrib.drt.extension.shifts.shift.DrtShiftBreak;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.core.utils.io.MatsimXmlWriter;
import org.matsim.core.utils.io.UncheckedIOException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nkuehnel
 */
public class DrtShiftsWriter extends MatsimXmlWriter {

    public static final String ROOT = "shifts";

    private final static String SHIFT_NAME = "shift";
    private final static String BREAK_NAME = "break";

    public static final String ID = "id";
    public static final String START_TIME = "start";
    public static final String END_TIME = "end";

    public static final String EARLIEST_BREAK_START_TIME = "earliestStart";
    public static final String LATEST_BREAK_END_TIME = "latestEnd";
    public static final String BREAK_DURATION = "duration";

    private static final Logger log = Logger.getLogger(DrtShiftsWriter.class);

    private final Map<Id<DrtShift>, ? extends DrtShift> shifts;

    private List<Tuple<String, String>> atts = new ArrayList<Tuple<String, String>>();

    public DrtShiftsWriter(DrtShifts shifts) {
        this.shifts = shifts.getShifts();
    }

    public void writeFile(String filename) {
        log.info( Gbl.aboutToWrite( "shifts", filename));
        openFile(filename);
        writeStartTag(ROOT, Collections.emptyList());
        try {
            writeShifts(shifts);
        } catch( IOException e ){
            e.printStackTrace();
        }
        writeEndTag(ROOT);
        close();
    }

    private void writeShifts(Map<Id<DrtShift>, ? extends DrtShift> shifts) throws UncheckedIOException, IOException {
        List<DrtShift> sortedShifts = shifts.values()
                .stream()
                .sorted(Comparator.comparing(DrtShift::getId))
                .collect(Collectors.toList());
        for (DrtShift shift : sortedShifts) {
            atts.clear();
            atts.add(createTuple(ID, shift.getId().toString()));
            atts.add(createTuple(START_TIME, shift.getStartTime()));
            atts.add(createTuple(END_TIME, shift.getEndTime()));
            this.writeStartTag(SHIFT_NAME, atts);

            //Write break, if present
            if (shift.getBreak() != null) {
                final DrtShiftBreak shiftBreak = shift.getBreak();
                atts.clear();
                atts.add(createTuple(EARLIEST_BREAK_START_TIME, shiftBreak.getEarliestBreakStartTime()));
                atts.add(createTuple(LATEST_BREAK_END_TIME, shiftBreak.getLatestBreakEndTime()));
                atts.add(createTuple(BREAK_DURATION, shiftBreak.getDuration()));
                this.writeStartTag(BREAK_NAME, atts, true);
            }
            this.writeEndTag(SHIFT_NAME);
        }
    }
}
