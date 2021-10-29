package com.unifun.sigproxy.sigtran.service.sccp;

import com.unifun.sigproxy.sigtran.exception.SS7NotContentException;
import com.unifun.sigproxy.sigtran.exception.SS7NotFoundException;
import com.unifun.sigproxy.sigtran.exception.SS7RemoveException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.models.config.sccp.*;
import com.unifun.sigproxy.sigtran.repository.SigtranStackRepository;
import com.unifun.sigproxy.sigtran.repository.sccp.*;
import com.unifun.sigproxy.sigtran.service.sccp.impl.SccpConfigServiceImpl;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //When
        sccpConfigService.getAddressConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getAddressConfigByStackId(id)).isEqualTo(sccpAddressConfigSet);
    }

    @Test
    void getAddressConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(new SigtranStack()));

        //When
        //verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getAddressConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
        Mockito.verify(sigtranStackRepository).findById(id);

    }

    @Test
    void getAddressConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getAddressConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }


    @Test
    void getAddressRuleConfigByRuleConfigId_Return_Value() {
        //given
        Integer id = 1;
        SccpRuleConfig sccpRuleConfig = new SccpRuleConfig();
        SccpAddressRuleConfig sccpAddressRuleConfig = new SccpAddressRuleConfig();
        sccpRuleConfig.setSccpAddressRuleConfig(sccpAddressRuleConfig);
        BDDMockito.given(sccpRuleConfigRepository.findById(id)).willReturn(Optional.of(sccpRuleConfig));

        //when
        sccpConfigService.getAddressRuleConfigByRuleConfigId(id);

        //verify
        Mockito.verify(sccpRuleConfigRepository).findById(id);
        assertThat(sccpConfigService.getAddressRuleConfigByRuleConfigId(id)).isEqualTo(sccpAddressRuleConfig);
    }

    @Test
    void getAddressRuleConfigByRuleConfigId_Throw_SS7NotContentException() {

        //given
        Integer id = 1;
        SccpRuleConfig sccpRuleConfig = new SccpRuleConfig();
        BDDMockito.given(sccpRuleConfigRepository.findById(id)).willReturn(Optional.of(sccpRuleConfig));

        //when
        //verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getAddressRuleConfigByRuleConfigId(id)).isInstanceOf(SS7NotContentException.class);
    }

    @Test
    void getAddressRuleConfigByRuleConfigId_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;

        //when
        //verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getAddressRuleConfigByRuleConfigId(id)).isInstanceOf(SS7NotFoundException.class);
    }


    @Test
    void getConcernedSignalingPointCodeConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        Set<SccpConcernedSignalingPointCodeConfig> sccpConcernedSignalingPointCodeConfigSet = new HashSet<>();
        sigtranStack.setSccpConcernedSignalingPointCodeConfigs(sccpConcernedSignalingPointCodeConfigSet);
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when
        sccpConfigService.getConcernedSignalingPointCodeConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getConcernedSignalingPointCodeConfigByStackId(id))
                .isEqualTo(sccpConcernedSignalingPointCodeConfigSet);


    }

    @Test
    void getConcernedSignalingPointCodeConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getConcernedSignalingPointCodeConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
    }

    @Test
    void getConcernedSignalingPointCodeConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getConcernedSignalingPointCodeConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getLongMessageRuleConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        Set<SccpLongMessageRuleConfig> sccpLongMessageRuleConfigSet = new HashSet<>();
        sigtranStack.setSccpLongMessageRuleConfigs(sccpLongMessageRuleConfigSet);
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when
        sccpConfigService.getLongMessageRuleConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getLongMessageRuleConfigByStackId(id))
                .isEqualTo(sccpLongMessageRuleConfigSet);

    }

    @Test
    void getLongMessageRuleConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getLongMessageRuleConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
    }

    @Test
    void getLongMessageRuleConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getLongMessageRuleConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }

    @Test
    void getMtp3DestinationConfigBySccpServiceAccessPointConfigId_Return_Value() {
        //given
        Integer id = 1;
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = new SccpServiceAccessPointConfig();
        Set<SccpMtp3DestinationConfig> sccpMtp3DestinationConfigSet = new HashSet<>();
        sccpServiceAccessPointConfig.setSccpMtp3DestinationConfigs(sccpMtp3DestinationConfigSet);
        BDDMockito.given(sccpServiceAccessPointConfigRepository.findById(id)).willReturn(Optional.of(sccpServiceAccessPointConfig));

        //when
        sccpConfigService.getMtp3DestinationConfigBySccpServiceAccessPointConfigId(id);

        //verify
        Mockito.verify(sccpServiceAccessPointConfigRepository).findById(id);
        assertThat(sccpConfigService.getMtp3DestinationConfigBySccpServiceAccessPointConfigId(id))
                .isEqualTo(sccpMtp3DestinationConfigSet);
    }

    @Test
    void getMtp3DestinationConfigBySccpServiceAccessPointConfigId_Throw_SS7NotContentException() {
        //given
        Integer id = 1;
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = new SccpServiceAccessPointConfig();
        BDDMockito.given(sccpServiceAccessPointConfigRepository.findById(id)).willReturn(Optional.of(sccpServiceAccessPointConfig));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getMtp3DestinationConfigBySccpServiceAccessPointConfigId(id))
                .isInstanceOf(SS7NotContentException.class);
    }

    @Test
    void getMtp3DestinationConfigBySccpServiceAccessPointConfigId_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getMtp3DestinationConfigBySccpServiceAccessPointConfigId(id))
                .isInstanceOf(SS7NotFoundException.class);
    }


    @Test
    void getRemoteSignalingPointConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        Set<SccpRemoteSignalingPointConfig> sccpRemoteSignalingPointConfigs = new HashSet<>();
        sigtranStack.setSccpRemoteSignalingPointConfigs(sccpRemoteSignalingPointConfigs);
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when
        sccpConfigService.getRemoteSignalingPointConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getRemoteSignalingPointConfigByStackId(id))
                .isEqualTo(sccpRemoteSignalingPointConfigs);
    }

    @Test
    void getRemoteSignalingPointConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRemoteSignalingPointConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
    }

    @Test
    void getRemoteSignalingPointConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRemoteSignalingPointConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }

    @Test
    void getRemoteSubsystemConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        Set<SccpRemoteSubsystemConfig> sccpRemoteSubsystemConfigs = new HashSet<>();
        sigtranStack.setSccpRemoteSubsystemConfigs(sccpRemoteSubsystemConfigs);
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when
        sccpConfigService.getRemoteSubsystemConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getRemoteSubsystemConfigByStackId(id))
                .isEqualTo(sccpRemoteSubsystemConfigs);
    }

    @Test
    void getRemoteSubsystemConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRemoteSubsystemConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
    }

    @Test
    void getRemoteSubsystemConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRemoteSubsystemConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }

    @Test
    void getRuleConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        Set<SccpRuleConfig> sccpRuleConfigs = new HashSet<>();
        sigtranStack.setSccpRuleConfigs(sccpRuleConfigs);
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when
        sccpConfigService.getRuleConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getRuleConfigByStackId(id))
                .isEqualTo(sccpRuleConfigs);
    }

    @Test
    void getRuleConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRuleConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);

    }

    @Test
    void getRuleConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRuleConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getServiceAccessPointConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        Set<SccpServiceAccessPointConfig> sccpServiceAccessPointConfigs = new HashSet<>();
        sigtranStack.setSccpServiceAccessPointConfigs(sccpServiceAccessPointConfigs);
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when
        sccpConfigService.getServiceAccessPointConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getServiceAccessPointConfigByStackId(id))
                .isEqualTo(sccpServiceAccessPointConfigs);
    }

    @Test
    void getServiceAccessPointConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getServiceAccessPointConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);

    }

    @Test
    void getServiceAccessPointConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getServiceAccessPointConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }

    @Test
    void getSettingsConfigByStackId_Return_Value() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        SccpSettingsConfig sccpSettingsConfig = new SccpSettingsConfig();
        sigtranStack.setSccpSettingsConfig(sccpSettingsConfig);
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when
        sccpConfigService.getSettingsConfigByStackId(id);

        //verify
        Mockito.verify(sigtranStackRepository).findById(id);
        assertThat(sccpConfigService.getSettingsConfigByStackId(id))
                .isEqualTo(sccpSettingsConfig);
    }

    @Test
    void getSettingsConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        BDDMockito.given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getSettingsConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);

    }

    @Test
    void getSettingsConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getSettingsConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }


    @Test
    void getAddressConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpAddressConfig sccpAddressConfig = new SccpAddressConfig();
        BDDMockito.given(sccpAddressConfigRepository.findById(id)).willReturn(Optional.of(sccpAddressConfig));

        //when
        sccpConfigService.getAddressConfigById(id);

        //verify
        Mockito.verify(sccpAddressConfigRepository).findById(id);
        assertThat(sccpConfigService.getAddressConfigById(id))
                .isEqualTo(sccpAddressConfig);
    }

    @Test
    void getAddressConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getAddressConfigById(id)).isInstanceOf(SS7NotFoundException.class);

    }

    @Test
    void getAddressRuleConfigById_Return_Value() {
        //given
        Long id = 1L;
        SccpAddressRuleConfig sccpAddressRuleConfig = new SccpAddressRuleConfig();
        BDDMockito.given(sccpAddressRuleConfigRepository.findById(id)).willReturn(Optional.of(sccpAddressRuleConfig));

        //when
        sccpConfigService.getAddressRuleConfigById(id);

        //verify
        Mockito.verify(sccpAddressRuleConfigRepository).findById(id);
        assertThat(sccpConfigService.getAddressRuleConfigById(id)).isEqualTo(sccpAddressRuleConfig);

    }

    @Test
    void getAddressRuleConfigById_Return_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getAddressRuleConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getConcernedSignalingPointCodeConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig =
                new SccpConcernedSignalingPointCodeConfig();
        BDDMockito.given(sccpConcernedSignalingPointCodeConfigRepository.findById(id))
                .willReturn(Optional.of(sccpConcernedSignalingPointCodeConfig));

        //when
        sccpConfigService.getConcernedSignalingPointCodeConfigById(id);

        //verify
        Mockito.verify(sccpConcernedSignalingPointCodeConfigRepository).findById(id);
        assertThat(sccpConfigService.getConcernedSignalingPointCodeConfigById(id))
                .isEqualTo(sccpConcernedSignalingPointCodeConfig);
    }

    @Test
    void getConcernedSignalingPointCodeConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getConcernedSignalingPointCodeConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getLongMessageRuleConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpLongMessageRuleConfig sccpLongMessageRuleConfig = new SccpLongMessageRuleConfig();
        BDDMockito.given(sccpLongMessageRuleConfigRepository.findById(id)).willReturn(Optional.of(sccpLongMessageRuleConfig));

        //when
        sccpConfigService.getLongMessageRuleConfigById(id);

        //verify
        Mockito.verify(sccpLongMessageRuleConfigRepository).findById(id);
        assertThat(sccpConfigService.getLongMessageRuleConfigById(id)).isEqualTo(sccpLongMessageRuleConfig);

    }

    @Test
    void getLongMessageRuleConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getLongMessageRuleConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getMtp3DestinationConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpMtp3DestinationConfig sccpMtp3DestinationConfig = new SccpMtp3DestinationConfig();
        BDDMockito.given(sccpMtp3DestinationConfigRepository.findById(id)).willReturn(Optional.of(sccpMtp3DestinationConfig));

        //when
        sccpConfigService.getMtp3DestinationConfigById(id);

        //verify
        Mockito.verify(sccpMtp3DestinationConfigRepository).findById(id);
        assertThat(sccpConfigService.getMtp3DestinationConfigById(id)).isEqualTo(sccpMtp3DestinationConfig);

    }

    @Test
    void getMtp3DestinationConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getMtp3DestinationConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getRemoteSignalingPointConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig = new SccpRemoteSignalingPointConfig();
        BDDMockito.given(sccpRemoteSignalingPointConfigRepository.findById(id)).willReturn(Optional.of(sccpRemoteSignalingPointConfig));

        //when
        sccpConfigService.getRemoteSignalingPointConfigById(id);

        //verify
        Mockito.verify(sccpRemoteSignalingPointConfigRepository).findById(id);
        assertThat(sccpConfigService.getRemoteSignalingPointConfigById(id)).isEqualTo(sccpRemoteSignalingPointConfig);

    }

    @Test
    void getRemoteSignalingPointConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRemoteSignalingPointConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getRemoteSubsystemConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig = new SccpRemoteSubsystemConfig();
        BDDMockito.given(sccpRemoteSubsystemConfigRepository.findById(id)).willReturn(Optional.of(sccpRemoteSubsystemConfig));

        //when
        sccpConfigService.getRemoteSubsystemConfigById(id);

        //verify
        Mockito.verify(sccpRemoteSubsystemConfigRepository).findById(id);
        assertThat(sccpConfigService.getRemoteSubsystemConfigById(id)).isEqualTo(sccpRemoteSubsystemConfig);
    }

    @Test
    void getRemoteSubsystemConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRemoteSubsystemConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void getRuleConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpRuleConfig sccpRuleConfig = new SccpRuleConfig();
        BDDMockito.given(sccpRuleConfigRepository.findById(id)).willReturn(Optional.of(sccpRuleConfig));

        //when
        sccpConfigService.getRuleConfigById(id);

        //verify
        Mockito.verify(sccpRuleConfigRepository).findById(id);
        assertThat(sccpConfigService.getRuleConfigById(id)).isEqualTo(sccpRuleConfig);
    }

    @Test
    void getRuleConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getRuleConfigById(id)).isInstanceOf(SS7NotFoundException.class);

    }


    @Test
    void getServiceAccessPointConfigById_Return_Value() {
        //given
        Integer id = 1;
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = new SccpServiceAccessPointConfig();
        BDDMockito.given(sccpServiceAccessPointConfigRepository.findById(id)).willReturn(Optional.of(sccpServiceAccessPointConfig));

        //when
        sccpConfigService.getServiceAccessPointConfigById(id);

        //verify
        Mockito.verify(sccpServiceAccessPointConfigRepository).findById(id);
        assertThat(sccpConfigService.getServiceAccessPointConfigById(id)).isEqualTo(sccpServiceAccessPointConfig);
    }

    @Test
    void getServiceAccessPointConfigById_Throw_SS7NotFoundException() {
        //given
        Integer id = 1;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getServiceAccessPointConfigById(id)).isInstanceOf(SS7NotFoundException.class);

    }


    @Test
    void getSettingsConfigById_Return_Value() {
        //given
        Long id = 1L;
        SccpSettingsConfig sccpSettingsConfig = new SccpSettingsConfig();
        BDDMockito.given(sccpSettingsConfigRepository.findById(id)).willReturn(Optional.of(sccpSettingsConfig));

        //when
        sccpConfigService.getSettingsConfigById(id);

        //verify
        Mockito.verify(sccpSettingsConfigRepository).findById(id);
        assertThat(sccpConfigService.getSettingsConfigById(id)).isEqualTo(sccpSettingsConfig);

    }

    @Test
    void getSettingsConfigById_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.getSettingsConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    void removeAddressConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeAddressConfig(id);
    }

    @Test
    void removeAddressConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpAddressConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeAddressConfig(id)).isInstanceOf(SS7RemoveException.class);
    }

    @Test
    void removeAddressRuleConfig_Succes_Delete() {
        //given
        Long id = 1L;
        //when-verify
        sccpConfigService.removeAddressRuleConfig(id);
    }

    @Test
    void removeAddressRuleConfig_Throw_SS7RemoveException() {
        //given
        Long id = 1L;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpAddressRuleConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeAddressRuleConfig(id)).isInstanceOf(SS7RemoveException.class);
    }

    @Test
    void removeConcernedSignalingPointCodeConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeConcernedSignalingPointCodeConfig(id);
    }

    @Test
    void removeConcernedSignalingPointCodeConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpConcernedSignalingPointCodeConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeConcernedSignalingPointCodeConfig(id)).isInstanceOf(SS7RemoveException.class);
    }

    @Test
    void removeLongMessageRuleConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeLongMessageRuleConfig(id);
    }

    @Test
    void removeLongMessageRuleConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpLongMessageRuleConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeLongMessageRuleConfig(id)).isInstanceOf(SS7RemoveException.class);

    }

    @Test
    void removeMtp3DestinationConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeMtp3DestinationConfig(id);
    }

    @Test
    void removeMtp3DestinationConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpMtp3DestinationConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeMtp3DestinationConfig(id)).isInstanceOf(SS7RemoveException.class);
    }

    @Test
    void removeRemoteSignalingPointConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeRemoteSignalingPointConfig(id);
    }

    @Test
    void removeRemoteSignalingPointConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpRemoteSignalingPointConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeRemoteSignalingPointConfig(id)).isInstanceOf(SS7RemoveException.class);

    }

    @Test
    void removeRemoteSubsystemConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeRemoteSubsystemConfig(id);
    }

    @Test
    void removeRemoteSubsystemConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpRemoteSubsystemConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeRemoteSubsystemConfig(id)).isInstanceOf(SS7RemoveException.class);

    }

    @Test
    void removeRuleConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeRuleConfig(id);
    }

    @Test
    void removeRuleConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpRuleConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeRuleConfig(id)).isInstanceOf(SS7RemoveException.class);

    }

    @Test
    void removeServiceAccessPointConfig_Succes_Delete() {
        //given
        Integer id = 1;
        //when-verify
        sccpConfigService.removeServiceAccessPointConfig(id);
    }

    void removeServiceAccessPointConfig_Throw_SS7RemoveException() {
        //given
        Integer id = 1;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpServiceAccessPointConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeServiceAccessPointConfig(id)).isInstanceOf(SS7RemoveException.class);

    }

    @Test
    void removeSettingsConfig_Succes_Delete() {
        //given
        Long id = 1L;
        //when-verify
        sccpConfigService.removeSettingsConfig(id);
    }

    @Test
    void removeSettingsConfig_Throw_SS7RemoveException() {
        //given
        Long id = 1L;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(sccpSettingsConfigRepository).deleteById(id);
        //when-verify
        Assertions.assertThatThrownBy(() -> sccpConfigService.removeSettingsConfig(id)).isInstanceOf(SS7RemoveException.class);

    }

    @Test
    void addAddressConfig_Succes_Save() {
        //given
        SccpAddressConfig sccpAddressConfig = new SccpAddressConfig();
        BDDMockito.given(sccpAddressConfigRepository.save(sccpAddressConfig)).willReturn(sccpAddressConfig);
        ArgumentCaptor<SccpAddressConfig> captor = ArgumentCaptor.forClass(SccpAddressConfig.class);
        //when
        sccpConfigService.addAddressConfig(sccpAddressConfig);

        //verify
        Mockito.verify(sccpAddressConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpAddressConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addAddressConfig(sccpAddressConfig)).isEqualTo(captor.getValue());
    }

    @Test
    void addAddressRuleConfig_Succes_Save() {
        //given
        SccpAddressRuleConfig sccpAddressRuleConfig = new SccpAddressRuleConfig();
        BDDMockito.given(sccpAddressRuleConfigRepository.save(sccpAddressRuleConfig)).willReturn(sccpAddressRuleConfig);
        ArgumentCaptor<SccpAddressRuleConfig> captor = ArgumentCaptor.forClass(SccpAddressRuleConfig.class);
        //when
        sccpConfigService.addAddressRuleConfig(sccpAddressRuleConfig);

        //verify
        Mockito.verify(sccpAddressRuleConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpAddressRuleConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addAddressRuleConfig(sccpAddressRuleConfig)).isEqualTo(captor.getValue());

    }

    @Test
    void addConcernedSignalingPointCodeConfig_Succes_Save() {
        //given
        SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig = new SccpConcernedSignalingPointCodeConfig();
        BDDMockito.given(sccpConcernedSignalingPointCodeConfigRepository.save(sccpConcernedSignalingPointCodeConfig))
                .willReturn(sccpConcernedSignalingPointCodeConfig);
        ArgumentCaptor<SccpConcernedSignalingPointCodeConfig> captor = ArgumentCaptor.forClass(SccpConcernedSignalingPointCodeConfig.class);
        //when
        sccpConfigService.addConcernedSignalingPointCodeConfig(sccpConcernedSignalingPointCodeConfig);

        //verify
        Mockito.verify(sccpConcernedSignalingPointCodeConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpConcernedSignalingPointCodeConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addConcernedSignalingPointCodeConfig(sccpConcernedSignalingPointCodeConfig)).isEqualTo(captor.getValue());
    }

    @Test
    void addLongMessageRuleConfig_Succes_Save() {
        //given
        SccpLongMessageRuleConfig sccpLongMessageRuleConfig = new SccpLongMessageRuleConfig();
        BDDMockito.given(sccpLongMessageRuleConfigRepository.save(sccpLongMessageRuleConfig))
                .willReturn(sccpLongMessageRuleConfig);
        ArgumentCaptor<SccpLongMessageRuleConfig> captor = ArgumentCaptor.forClass(SccpLongMessageRuleConfig.class);
        //when
        sccpConfigService.addLongMessageRuleConfig(sccpLongMessageRuleConfig);

        //verify
        Mockito.verify(sccpLongMessageRuleConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpLongMessageRuleConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addLongMessageRuleConfig(sccpLongMessageRuleConfig))
                .isEqualTo(captor.getValue());

    }

    @Test
    void addMtp3DestinationConfig_Succes_Save() {
        //given
        SccpMtp3DestinationConfig sccpMtp3DestinationConfig = new SccpMtp3DestinationConfig();
        BDDMockito.given(sccpMtp3DestinationConfigRepository.save(sccpMtp3DestinationConfig)).willReturn(sccpMtp3DestinationConfig);
        ArgumentCaptor<SccpMtp3DestinationConfig> captor = ArgumentCaptor.forClass(SccpMtp3DestinationConfig.class);
        //when
        sccpConfigService.addMtp3DestinationConfig(sccpMtp3DestinationConfig);

        //verify
        Mockito.verify(sccpMtp3DestinationConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpMtp3DestinationConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addMtp3DestinationConfig(sccpMtp3DestinationConfig))
                .isEqualTo(captor.getValue());

    }

    @Test
    void addRemoteSignalingPointConfig_Succes_Save() {
        //given
        SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig = new SccpRemoteSignalingPointConfig();
        BDDMockito.given(sccpRemoteSignalingPointConfigRepository.save(sccpRemoteSignalingPointConfig)).willReturn(sccpRemoteSignalingPointConfig);
        ArgumentCaptor<SccpRemoteSignalingPointConfig> captor = ArgumentCaptor.forClass(SccpRemoteSignalingPointConfig.class);
        //when
        sccpConfigService.addRemoteSignalingPointConfig(sccpRemoteSignalingPointConfig);

        //verify
        Mockito.verify(sccpRemoteSignalingPointConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpRemoteSignalingPointConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addRemoteSignalingPointConfig(sccpRemoteSignalingPointConfig))
                .isEqualTo(captor.getValue());
    }

    @Test
    void addRemoteSubsystemConfig_Succes_Save() {
        //given
        SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig = new SccpRemoteSubsystemConfig();
        BDDMockito.given(sccpRemoteSubsystemConfigRepository.save(sccpRemoteSubsystemConfig)).willReturn(sccpRemoteSubsystemConfig);
        ArgumentCaptor<SccpRemoteSubsystemConfig> captor = ArgumentCaptor.forClass(SccpRemoteSubsystemConfig.class);
        //when
        sccpConfigService.addRemoteSubsystemConfig(sccpRemoteSubsystemConfig);

        //verify
        Mockito.verify(sccpRemoteSubsystemConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpRemoteSubsystemConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addRemoteSubsystemConfig(sccpRemoteSubsystemConfig))
                .isEqualTo(captor.getValue());
    }

    @Test
    void addRuleConfig() {
        //given
        SccpRuleConfig sccpRuleConfig = new SccpRuleConfig();
        BDDMockito.given(sccpRuleConfigRepository.save(sccpRuleConfig)).willReturn(sccpRuleConfig);
        ArgumentCaptor<SccpRuleConfig> captor = ArgumentCaptor.forClass(SccpRuleConfig.class);
        //when
        sccpConfigService.addRuleConfig(sccpRuleConfig);

        //verify
        Mockito.verify(sccpRuleConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpRuleConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addRuleConfig(sccpRuleConfig))
                .isEqualTo(captor.getValue());
    }

    @Test
    void addServiceAccessPointConfig() {
        //given
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = new SccpServiceAccessPointConfig();
        BDDMockito.given(sccpServiceAccessPointConfigRepository.save(sccpServiceAccessPointConfig)).willReturn(sccpServiceAccessPointConfig);
        ArgumentCaptor<SccpServiceAccessPointConfig> captor = ArgumentCaptor.forClass(SccpServiceAccessPointConfig.class);
        //when
        sccpConfigService.addServiceAccessPointConfig(sccpServiceAccessPointConfig);

        //verify
        Mockito.verify(sccpServiceAccessPointConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpServiceAccessPointConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addServiceAccessPointConfig(sccpServiceAccessPointConfig))
                .isEqualTo(captor.getValue());
    }

    @Test
    void addSettingsConfig() {
        //given
        SccpSettingsConfig sccpSettingsConfig = new SccpSettingsConfig();
        BDDMockito.given(sccpSettingsConfigRepository.save(sccpSettingsConfig)).willReturn(sccpSettingsConfig);
        ArgumentCaptor<SccpSettingsConfig> captor = ArgumentCaptor.forClass(SccpSettingsConfig.class);
        //when
        sccpConfigService.addSettingsConfig(sccpSettingsConfig);

        //verify
        Mockito.verify(sccpSettingsConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(sccpSettingsConfig);
        AssertionsForClassTypes.assertThat(sccpConfigService.addSettingsConfig(sccpSettingsConfig))
                .isEqualTo(captor.getValue());
    }
}
