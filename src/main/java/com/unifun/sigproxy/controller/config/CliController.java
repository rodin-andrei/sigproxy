package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.service.SigtranConfigServiceImpl;
import com.unifun.sigproxy.service.sctp.impl.SctpConfigServiceImpl;
import com.unifun.sigproxy.service.sctp.impl.SctpServiceImpl;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
@RequestMapping("conf/cli")
@RequiredArgsConstructor
public class CliController {
    private final SigtranConfigServiceImpl sigtranConfigService;
    private final SctpConfigServiceImpl sctpConfigService;
    private final SctpServiceImpl sctpService;

    @GetMapping(value = "/getAnswer/{command}", produces = "application/json")
    @ResponseBody
    public String getAnswer(@PathVariable String command) {
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.getContext().setGridTheme(TA_GridThemes.HORIZONTAL);
        StringBuilder sb = new StringBuilder();
        String[] commandWords = command.split(" ");
        long lastChar = 1L;
        if (commandWords[commandWords.length - 1].matches(".*\\d+$") && commandWords.length > 1) {
            lastChar = Long.parseLong(commandWords[commandWords.length - 1]);
            command = "";
            for (int i = 0; i < commandWords.length - 1; i++) {
                command = sb.append(" ").append(commandWords[i]).toString();
            }
        }
        return switch (command.trim()) {
            case "stack show list" -> stackShowList(asciiTable);
            case "sctp links show stack" -> sctpLinksShowStack(asciiTable, lastChar);
            case "sctp servers show stack" -> sctpServersShowStack(asciiTable, lastChar);
            default -> "Unknown command";
        };
    }

    private String stackShowList(AsciiTable asciiTable) {
        asciiTable.addRule();
        asciiTable.addRow("ID", "Stack Name").setTextAlignment(TextAlignment.CENTER);
        asciiTable.addRule();
        for (SigtranStack stack : sigtranConfigService.getSigtranStacks()
        ) {
            asciiTable.addRow(
                    Objects.toString(stack.getId(), ""),
                    Objects.toString(stack.getStackName(), "")).setTextAlignment(TextAlignment.CENTER);
            asciiTable.addRule();
        }
        return asciiTable.render();
    }

    private String sctpLinksShowStack(AsciiTable asciiTable, long stackId) {
        asciiTable.addRule();
        asciiTable.addRow("ID", "Link Name", "Remote Address", "Remote Port", "Local Address", "Status")
                .setTextAlignment(TextAlignment.CENTER);
        asciiTable.addRule();
        for (SctpClientAssociationConfig config : sctpConfigService.getSctpClientAssociationConfigByStackId(stackId)
        ) {
            asciiTable.addRow(
                    Objects.toString(config.getId(), ""),
                    Objects.toString(config.getLinkName(), ""),
                    Objects.toString(config.getRemoteAddress(), ""),
                    Objects.toString(config.getRemotePort(), ""),
                    Objects.toString(config.getLocalAddress(), ""),
                    Objects.toString(sctpService.getLinkStatus(config.getSigtranStack().getStackName(),
                            config.getLinkName()), ""))
                    .setTextAlignment(TextAlignment.CENTER);
            asciiTable.addRule();
        }
        return asciiTable.render();
    }

    private String sctpServersShowStack(AsciiTable asciiTable, long stackId) {
        StringBuilder result = new StringBuilder();
        asciiTable.addRule();
        asciiTable.addRow("ID", "Server Name", "Local Address", "Local Port", "Extra Addresses")
                .setTextAlignment(TextAlignment.CENTER);
        asciiTable.addRule();
        for (SctpServerConfig config : sctpConfigService.getSctpServerConfigByStackId(stackId)
        ) {
            AsciiTable asciiTableExtra = new AsciiTable();
            asciiTableExtra.getContext().setGridTheme(TA_GridThemes.HORIZONTAL);
            asciiTable.addRow(
                    Objects.toString(config.getId(), ""),
                    Objects.toString(config.getName(), ""),
                    Objects.toString(config.getLocalAddress(), ""),
                    Objects.toString(config.getLocalPort(), ""),
                    Objects.toString(config.getMultihomingAddresses(), ""))
                    .setTextAlignment(TextAlignment.CENTER);
            asciiTable.addRule();
            asciiTableExtra.addRow("ID", "Link Name", "Remote Address", "Remote Port", "Status")
                    .setTextAlignment(TextAlignment.CENTER);
            asciiTableExtra.addRule();
            for (SctpServerAssociationConfig associationConfig : config.getSctpServerAssociationConfigs()) {
                asciiTableExtra.addRow(
                        Objects.toString(associationConfig.getId(), ""),
                        Objects.toString(associationConfig.getLinkName(), ""),
                        Objects.toString(associationConfig.getRemoteAddress(), ""),
                        Objects.toString(associationConfig.getRemotePort(), ""),
                        Objects.toString(sctpService.getLinkStatus(config.getSigtranStack().getStackName(),
                                associationConfig.getLinkName()), ""))
                        .setTextAlignment(TextAlignment.CENTER);
                asciiTableExtra.addRule();
            }
            result.append(asciiTable.render()).append("\n").append(asciiTableExtra.render()).append("\n");
        }
        return String.valueOf(result);
    }
}
