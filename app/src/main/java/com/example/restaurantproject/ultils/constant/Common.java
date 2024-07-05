package com.example.restaurantproject.ultils.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Common {
    public static final List<String> tableStatus = Collections.unmodifiableList(
            Arrays.asList("Empty table", "Live Table", "Error table")
    );
}
