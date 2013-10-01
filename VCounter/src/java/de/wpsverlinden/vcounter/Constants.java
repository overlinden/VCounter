/**
 *
 * VCounter - Der Besucherzähler ohne JavaScript!
 * Copyright (C) 2013 Oliver Verlinden (http://wps-verlinden.de)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.wpsverlinden.vcounter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Constants {

    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    public static final DateFormat TIME_FORMAT_DEU = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static final DateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat DAY_FORMAT_DEU = new SimpleDateFormat("dd.MM.yyyy");

    public static final DateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");
    public static final DateFormat MONTH_FORMAT_DEU = new SimpleDateFormat("MM.yyyy");
    public static final DateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");

}
