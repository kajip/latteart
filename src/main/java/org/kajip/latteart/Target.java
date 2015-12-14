package org.kajip.latteart;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.management.ObjectName;

@AllArgsConstructor
@EqualsAndHashCode
public class Target {

    public final ObjectName objectName;

    public final String[] attributeNames;
}
