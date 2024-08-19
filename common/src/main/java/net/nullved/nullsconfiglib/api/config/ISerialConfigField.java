package net.nullved.nullsconfiglib.api.config;


import net.nullved.nullsconfiglib.api.config.autogen.Comment;

import java.util.List;
import java.util.Optional;

/**
 * The backing interface for {@link ConfigEntry} annotations
 */
public interface ISerialConfigField {
    String serialName();
    Optional<List<Comment>> comment();
    boolean required();
    boolean nullable();
}
