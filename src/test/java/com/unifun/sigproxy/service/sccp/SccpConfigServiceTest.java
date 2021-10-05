package com.unifun.sigproxy.service.sccp;

import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sccp.SccpAddressConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.sccp.*;
import com.unifun.sigproxy.service.sccp.impl.SccpConfigServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SccpConfigServiceTest {

    private SccpConfigService sccpConfigService;

    @Mock
    private SigtranStackRepository sigtranStackRepository;
    @Mock
    private SccpAddressConfigRepository sccpAddressConfigRepository;
    @Mock
    private SccpAddressRuleConfigRepository sccpAddressRuleConfigRepository;
    @Mock
    private SccpConcernedSignalingPointCodeConfigRepository sccpConcernedSignalingPointCodeConfigRepository;
    @Mock
    private SccpLongMessageRuleConfigRepository sccpLongMessageRuleConfigRepository;
    @Mock
    private SccpMtp3DestinationConfigRepository sccpMtp3DestinationConfigRepository;
    @Mock
    private SccpRemoteSignalingPointConfigRepository sccpRemoteSignalingPointConfigRepository;
    @Mock
    private SccpRemoteSubsystemConfigRepository sccpRemoteSubsystemConfigRepository;
    @Mock
    private SccpRuleConfigRepository sccpRuleConfigRepository;
    @Mock
    private SccpServiceAccessPointConfigRepository sccpServiceAccessPointConfigRepository;
    @Mock
    private SccpSettingsConfigRepository sccpSettingsConfigRepository;


    @BeforeEach
    void setUp() {
        this.sccpConfigService = new SccpConfigServiceImpl(sigtranStackRepository, sccpAddressConfigRepository,
                sccpAddressRuleConfigRepository, sccpConcernedSignalingPointCodeConfigRepository,
                sccpLongMessageRuleConfigRepository, sccpMtp3DestinationConfigRepository,
                sccpRemoteSignalingPointConfigRepository, sccpRemoteSubsystemConfigRepository, sccpRuleConfigRepository,
                sccpServiceAccessPointConfigRepository, sccpSettingsConfigRepository);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void getAddressConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        Set<SccpAddressConfig> sccpAddressConfigSet = new HashSet<>();
        sigtranStack.setSccpAddressConfigs(new HashSet<>(sccpAddressConfigSet));
        given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //When
        sccpConfigService.getAddressConfigByStackId(id);

        //verify
        verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getAddressConfigByStackId(id)).isEqualTo(sccpAddressConfigSet);
    }

    @Test
    void getAddressConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        given(sigtranStackRepository.findById(id)).willReturn(Optional.of(new SigtranStack()));

        //When
        //verify
        assertThatThrownBy(() -> sccpConfigService.getAddressConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
        verify(sigtranStackRepository).findById(id);

    }

    @Test
    void getAddressConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;

        //When
        //verify
        verify(sigtranStackRepository).findById(id);
        assertThatThrownBy(() -> sccpConfigService.getAddressConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }


    @Test
    void getAddressRuleConfigByRuleConfigId_Return_Value() {
        //given
        Long id = 1L;

    }

    @Test
    void getAddressRuleConfigByRuleConfigId_Throw_SS7NotContentException() {

    }

    @Test
    void getAddressRuleConfigByRuleConfigId_Throw_SS7NotFoundException() {

    }


    @Test
    void getConcernedSignalingPointCodeConfigByStackId_Return_Value() {
    }

    @Test
    void getLongMessageRuleConfigByStackId() {
    }

    @Test
    void getMtp3DestinationConfigBySccpServiceAccessPointConfigId() {
    }

    @Test
    void getRemoteSignalingPointConfigByStackId() {
    }

    @Test
    void getRemoteSubsystemConfigByStackId() {
    }

    @Test
    void getRuleConfigByStackId() {
    }

    @Test
    void getServiceAccessPointConfigByStackId() {
    }

    @Test
    void getSettingsConfigByStackId() {
    }

    @Test
    void getAddressConfigById() {
    }

    @Test
    void getAddressRuleConfigById() {
    }

    @Test
    void getConcernedSignalingPointCodeConfigById() {
    }

    @Test
    void getLongMessageRuleConfigById() {
    }

    @Test
    void getMtp3DestinationConfigById() {
    }

    @Test
    void getRemoteSignalingPointConfigById() {
    }

    @Test
    void getRemoteSubsystemConfigById() {
    }

    @Test
    void getRuleConfigById() {
    }

    @Test
    void getServiceAccessPointConfigById() {
    }

    @Test
    void getSettingsConfigById() {
    }

    @Test
    void removeAddressConfig() {
    }

    @Test
    void removeAddressRuleConfig() {
    }

    @Test
    void removeConcernedSignalingPointCodeConfig() {
    }

    @Test
    void removeLongMessageRuleConfig() {
    }

    @Test
    void removeMtp3DestinationConfig() {
    }

    @Test
    void removeRemoteSignalingPointConfig() {
    }

    @Test
    void removeRemoteSubsystemConfig() {
    }

    @Test
    void removeRuleConfig() {
    }

    @Test
    void removeServiceAccessPointConfig() {
    }

    @Test
    void removeSettingsConfig() {
    }

    @Test
    void addAddressConfig() {
    }

    @Test
    void addAddressRuleConfig() {
    }

    @Test
    void addConcernedSignalingPointCodeConfig() {
    }

    @Test
    void addLongMessageRuleConfig() {
    }

    @Test
    void addMtp3DestinationConfig() {
    }

    @Test
    void addRemoteSignalingPointConfig() {
    }

    @Test
    void addRemoteSubsystemConfig() {
    }

    @Test
    void addRuleConfig() {
    }

    @Test
    void addServiceAccessPointConfig() {
    }

    @Test
    void addSettingsConfig() {
    }
}
