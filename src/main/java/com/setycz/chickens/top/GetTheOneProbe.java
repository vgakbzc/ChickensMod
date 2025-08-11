package com.setycz.chickens.top;


import com.google.common.base.Function;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nullable;

public class GetTheOneProbe implements Function<ITheOneProbe, Void>
{

    public static ITheOneProbe theOneProbe;

    @Nullable
    @Override
    public Void apply (ITheOneProbe input) {

        theOneProbe = input;
        theOneProbe.registerEntityProvider(new TheOneProbeEntityPlugin());
        theOneProbe.registerProvider(new TheOneProbeBlockPlugin());
        return null;
    }
}