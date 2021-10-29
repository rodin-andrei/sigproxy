package com.unifun.sigproxy.sigtran.service.rabbit;

import com.unifun.sigproxy.sigtran.service.rabbit.pojo.SccpRabbitAddress;
import com.unifun.sigproxy.sigtran.service.sccp.SccpParametersService;
import lombok.AllArgsConstructor;
import org.restcomm.protocols.ss7.indicator.GlobalTitleIndicator;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SccpParametersServiceImpl implements SccpParametersService {
    private final SccpServiceImpl sccpService;


    @Override
    public SccpAddressImpl createSccpAddress(SccpRabbitAddress address, String stackName) {
        int ssn = address.getSsn() != null ? address.getSsn() : -1;
        int pc = address.getPc() != null ? address.getPc() : -1;

        GlobalTitleIndicator globalTitleIndicator = GlobalTitleIndicator.valueOf(address.getGlobalTitleIndicator());

        GlobalTitle globalTitle = switch (globalTitleIndicator.name()) {
            case "0001",
                    "GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY",
                    "0010",
                    "GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY",
                    "0011",
                    "GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME" -> this.sccpService.getSccpParameterFactory(stackName)
                    .createGlobalTitle(address.getDigits(),
                            getNatureOfAddressFromString(address.getSccpNatureOfAddress()));
            default -> this.sccpService.getSccpParameterFactory(stackName).createGlobalTitle(address.getDigits(),
                    0,
                    getNumberingPlanFromString(address.getSccpNumberingPlan()),
                    null,
                    getNatureOfAddressFromString(address.getSccpNatureOfAddress()));
        };

        return new SccpAddressImpl(
                RoutingIndicator.valueOf(address.getRoutingIndicator()),
                globalTitle, pc, ssn);
    }


    @Override
    public SccpRabbitAddress createSccpRabbitAddress(SccpAddress sccpAddress) {
        SccpRabbitAddress sccpRabbitAddress = new SccpRabbitAddress();
        if (sccpAddress.getGlobalTitle() != null) {
            sccpRabbitAddress.setDigits(sccpAddress.getGlobalTitle().getDigits());
            sccpRabbitAddress.setSsn(sccpAddress.getSubsystemNumber());
            sccpRabbitAddress.setPc(sccpAddress.getSignalingPointCode());

            sccpRabbitAddress.setGlobalTitleIndicator(sccpAddress.getAddressIndicator().getGlobalTitleIndicator().getValue());

            switch (sccpAddress.getGlobalTitle().getGlobalTitleIndicator()) {
                case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                    sccpRabbitAddress.setSccpNatureOfAddress(((GlobalTitle0001) sccpAddress.getGlobalTitle()).getNatureOfAddress().name());
                    break;
                case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                    break;
                case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                    sccpRabbitAddress.setSccpEncodingSchema(((GlobalTitle0011) sccpAddress.getGlobalTitle()).getEncodingScheme().getType().name());
                    sccpRabbitAddress.setSccpNumberingPlan(((GlobalTitle0011) sccpAddress.getGlobalTitle()).getNumberingPlan().name());
                    break;
                case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                    sccpRabbitAddress.setSccpNatureOfAddress(((GlobalTitle0100) sccpAddress.getGlobalTitle()).getNatureOfAddress().name());
                    sccpRabbitAddress.setSccpEncodingSchema(((GlobalTitle0100) sccpAddress.getGlobalTitle()).getEncodingScheme().getType().name());
                    sccpRabbitAddress.setSccpNumberingPlan(((GlobalTitle0100) sccpAddress.getGlobalTitle()).getNumberingPlan().name());
                    break;
            }
        }

        if (sccpAddress.getAddressIndicator() != null) {
            sccpRabbitAddress.setRoutingIndicator(sccpAddress.getAddressIndicator().getRoutingIndicator().getValue());
        }
        return sccpRabbitAddress;
    }

    private NatureOfAddress getNatureOfAddressFromString(String na) {
        return NatureOfAddress.valueOf(na);
    }

    private NumberingPlan getNumberingPlanFromString(String np) {
        return NumberingPlan.valueOf(np);
    }
}
