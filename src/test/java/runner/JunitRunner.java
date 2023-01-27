package runner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.TC_DELETE_DeleteBooking;
import tests.TC_GET_BookingIds;
import tests.TC_PATCH_PartialUpdateBooking;

@RunWith(Suite.class)
@SuiteClasses({TC_PATCH_PartialUpdateBooking.class,TC_DELETE_DeleteBooking.class,TC_GET_BookingIds.class})
public class JunitRunner {

}
