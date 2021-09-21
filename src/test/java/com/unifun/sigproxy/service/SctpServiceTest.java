package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import com.unifun.sigproxy.service.sctp.impl.SctpConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;



@Slf4j
public class SctpServiceTest {

    @Mock
    private SctpServerRepository sctpServerRepository;

    @Mock
    private SigtranStackRepository sigtranStackRepository;

    @Mock
    private RemoteSctpLinkRepository remoteSctpLinkRepository;

    @Mock
    private SctpLinkRepository sctpLinkRepository;


    private SctpConfigService sctpConfigService;


    public SctpServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.sctpConfigService = new SctpConfigServiceImpl(sigtranStackRepository, remoteSctpLinkRepository, sctpLinkRepository, sctpServerRepository);
    }


    @Test
    public void getSctpServerConfig_ById_Should_Return_SctpServerConfig() {
        SctpServerConfig sctpServerConfig = new SctpServerConfig();
        sctpServerConfig.setLocalAddress("1.1.1.1");
        sctpServerConfig.setName("test");
        sctpServerConfig.setId(1L);
        given(sctpServerRepository.findById(1L)).willReturn(Optional.of(sctpServerConfig));
        SctpServerConfig test = sctpConfigService.getSctpServerById(1L);
        log.info("Test: SctpConfigService with sctpServerRepository ===>Expect result:" +
                "\n{Local Adress} = 1.1.1.1" +
                "\n{Name} = test" +
                "\n(id} = 1");
        log.info("Real result after Testing:  " +
                "\n{Local Adress} = " + test.getLocalAddress() +
                "\n{Name} = " + test.getName() +
                "\n(id} = " + test.getId());
        Assertions.assertThat(test).isNotNull();
        verify(sctpServerRepository).findById(1L);
    }

    @Test(expected = SS7NotFoundException.class)
    public void getSctpServerConfig_ById_Should_Return_Null() {
        given(sctpServerRepository.findById(1L)).willReturn(Optional.empty());
        Optional<SctpServerConfig> test = Optional.ofNullable(sctpConfigService.getSctpServerById(1L));
        Assertions.assertThat(test).isEmpty();
    }

    @Test
    public void save_SctpClientAssociationConfig_With_Rigth_Param(){
        SctpClientAssociationConfig s = new SctpClientAssociationConfig();
        s.setLinkName("test");
        s.setLocalAddress("test");
        s.setLocalPort(2020);
        s.setRemoteAddress("test");
        s.setLocalPort(8080);
        s.setId(1L);
        s.setMultihomingAddresses(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "t", "k", "k", "k", "l", "k"});
        s.setSigtranStack(new SigtranStack());
        given(sctpLinkRepository.save(s)).willReturn(s);
        ArgumentCaptor<SctpClientAssociationConfig> captor = ArgumentCaptor.forClass(SctpClientAssociationConfig.class);
        sctpConfigService.addClinetLink(s);
        verify(sctpLinkRepository).save(captor.capture());
        log.info("Succes save with: "+ captor.getValue());
        Assertions.assertThat(captor.getValue()).isEqualTo(s);
    }

    @Test
    public void save_SctpClientAssociationConfig_With_Wrong_Param(){
        SctpClientAssociationConfig s = new SctpClientAssociationConfig();
        s.setLinkName("test");
        s.setLocalAddress("test");
        s.setLocalPort(2020);
        s.setRemoteAddress("test");
        s.setLocalPort(8080);
        s.setId(1L);
        s.setMultihomingAddresses(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "t", "k", "k", "k", "l", "k"});
        s.setSigtranStack(new SigtranStack());
        given(sctpLinkRepository.save(s)).willReturn(s);
        ArgumentCaptor<SctpClientAssociationConfig> captor = ArgumentCaptor.forClass(SctpClientAssociationConfig.class);
        sctpConfigService.addClinetLink(anyObject());
        verify(sctpLinkRepository).save(captor.capture());
        log.info("Succes save with: "+ captor.getValue());
        Assertions.assertThat(captor.getValue()).isNotEqualTo(s);
    }


}
