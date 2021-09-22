package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import com.unifun.sigproxy.service.sctp.impl.SctpConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.restcomm.protocols.ss7.m3ua.As;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;


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
        Assertions.assertThat(sctpServerRepository.findById(1L)).isPresent();
    }

    @Test(expectedExceptions = SS7NotFoundException.class)
    public void getSctpServerConfig_ById_Should_Return_Null() {
        given(sctpServerRepository.findById(1L)).willReturn(Optional.empty());
        Optional<SctpServerConfig> test = Optional.ofNullable(sctpConfigService.getSctpServerById(1L));
        Assertions.assertThat(test).isEmpty();
    }

    @Test
    public void saveSctpClientAssociationConfig_With_Rigth_Param() {
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
        sctpConfigService.addClinetLink(s);
        Assertions.assertThat(sctpLinkRepository.save(s)).isEqualTo(s);
    }

    @Test
    public void saveSctpClientAssociationConfig_With_Wrong_Param() {
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
        verify(sctpLinkRepository, atLeastOnce()).save(captor.capture());
        Assertions.assertThat(captor.getValue()).isNotEqualTo(s);

    }


    @Test
    public void removeSctpClientAssociationConfig_With_Right_Id() {
        Long deletedId = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        sctpConfigService.removeClientLinkById(deletedId);
        verify(sctpLinkRepository, atLeastOnce()).deleteById(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(deletedId);
    }

    @Test
    public void getServerLinksBySctpServerId_Should_Return_Set() {
        SctpServerConfig sctpServerConfig = new SctpServerConfig();
        sctpServerConfig.setId(1L);
        Set<SctpServerAssociationConfig> sctpServerAssociationConfigSet = new HashSet<>();
        sctpServerAssociationConfigSet.add(new SctpServerAssociationConfig());
        sctpServerConfig.setSctpServerAssociationConfigs(sctpServerAssociationConfigSet);
        log.info("Start emulated sctpServerRepository .......");
        given(sctpServerRepository.findById(1L)).willReturn(Optional.of(sctpServerConfig));
        Optional<SctpServerConfig> sctpServerConfigTest = sctpServerRepository.findById(1L);
        if(sctpServerConfigTest.orElseGet(SctpServerConfig::new).getId() == 1L){
            log.info("sctpServerRepository was emulated successfully");
        } else {
            log.info("error: sctpServerRepository wasn't emulated");
        }

        Set<SctpServerAssociationConfig> sctpServerAssociationConfigSetTest = sctpConfigService.getServerLinksBySctpServerId(1L);
        verify(sctpServerRepository,atLeast(2)).findById(1L);
        Assertions.assertThat( sctpServerAssociationConfigSetTest).isEqualTo(sctpServerConfig.getSctpServerAssociationConfigs());
        log.info("sctpConfigService.getServerLinksBySctpServerId() Successful pass test");
        log.info("<==============================TEST========================================>");
    }


    @Test
    public void getClientLinkById_Should_Return_SctpClientAssociationConfig(){
        SctpClientAssociationConfig s = new SctpClientAssociationConfig();
        s.setId(1L);
        log.info("Start emulated SctpLinkRepository ...");
        given(sctpLinkRepository.findById(1L)).willReturn(Optional.of(s));

        if(sctpLinkRepository.findById(1L).isPresent()){
            log.info("Succes emulated SctpLinkRepository for findById(1L) method");
        } else {
            log.info("error: SctpLinkRepository wasn't emulated");
        }
        log.info("Start verification sctpConfigService.getClientLinkById(...) ....");
        Optional<SctpClientAssociationConfig> containerWithObject = Optional.of(sctpConfigService.getClientLinkById(1L));
        Assertions.assertThat(containerWithObject.get()).isEqualTo(s);
        log.info("sctpConfigService.getClientLinkById(...)  success pass verification !");
        log.info("<==============================TEST========================================>");    }

    @Test
    public void getSctpServerById_Should_Return_SctpServerConfig(){
        SctpServerConfig s = new SctpServerConfig();
        s.setId(1L);
        log.info("Start emulated sctpServerRepository .......");
        given(sctpServerRepository.findById(1L)).willReturn(Optional.of(s));
        Optional<SctpServerConfig> sctpServerConfigTest = sctpServerRepository.findById(1L);
        if(sctpServerConfigTest.orElseGet(SctpServerConfig::new).getId() == 1L){
            log.info("sctpServerRepository was emulated successfully");
        } else {
            log.info("error: sctpServerRepository wasn't emulated");
        }
        log.info("Start verification sctpConfigService.getSctpServerById(...) ....");
        Assertions.assertThat(sctpConfigService.getSctpServerById(1L)).isEqualTo(s);
        log.info("sctpConfigService.getSctpServerById(...)  success pass verification !");
        log.info("<==============================TEST========================================>");
    }

    @Test
    public void getServerLinkById_Should_Return_SctpServerAssociationConfig(){
        SctpServerAssociationConfig association = new SctpServerAssociationConfig();
        association.setId(1L);
        log.info("Start emulate remoteSctpLinkRepository ...");
        given(remoteSctpLinkRepository.findById(1L)).willReturn(Optional.of(association));
        Optional<SctpServerAssociationConfig> associationContainer = remoteSctpLinkRepository.findById(1L);
        if (associationContainer.isPresent()){
            log.info("remoteSctpLinkRepository succes emulated for  findById() method");
        } else {
            log.info("Incorrect emulation of remoteSctpLinkRepository");
        }
        log.info("Starting test for service ...");
        Assertions.assertThat(sctpConfigService.getServerLinkById(1L)).isEqualTo(association);
        log.info("sctpConfigService.getServerLinkById(...) succes passed test");
        log.info("<==============================TEST========================================>");
    }

    @Test
    public void removeServerLinkById_Should_Return_Void(){
        SctpServerAssociationConfig s = new SctpServerAssociationConfig();
        s.setId(1L);
        given(remoteSctpLinkRepository.findById(1L)).willReturn(Optional.of(s));
        Long deletedId = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        sctpConfigService.removeServerLinkById(deletedId);
        verify(remoteSctpLinkRepository, atLeastOnce()).deleteById(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(deletedId);
    }


    @Test
    public void getSctpServersByStackId_Should_Return_List(){
        SctpServerConfig sctpServerConfig = new SctpServerConfig();
        sctpServerConfig.setId(1L);
        List<SctpServerConfig> sctpServerConfigs = new ArrayList<>();
        sctpServerConfigs.add(sctpServerConfig);
        given(sctpServerRepository.findAll()).willReturn(sctpServerConfigs);

        List<SctpServerConfig> sctpServerConfigsTestRepo = sctpServerRepository.findAll();
        if (sctpServerConfigsTestRepo.isEmpty()){
            log.info("Incorrect emulation of sctpServerRepository");
        } else {
            log.info("sctpServerRepository success emulated for  findAll() method");
        }
        List<SctpServerConfig> sctpServerConfigsTestService = sctpConfigService.getSctpServersByStackId(1L);
        verify(sctpServerRepository,atLeastOnce()).findAll();
        Assertions.assertThat(sctpServerConfigsTestService.isEmpty()).isEqualTo(false);
    }

    @Test
    public void getClientLinksByStackId_Should_Return_Set(){
        SigtranStack sigtranStack = new SigtranStack();
        sigtranStack.setId(1L);
        Set<SctpClientAssociationConfig> sctpClientAssociationConfigs = new HashSet<>();
        sctpClientAssociationConfigs.add(new SctpClientAssociationConfig());
        sigtranStack.setAssociations(sctpClientAssociationConfigs);
        given(sigtranStackRepository.findById(1L)).willReturn(Optional.of(sigtranStack));
        sctpConfigService.getClientLinksByStackId(1L);
        verify(sigtranStackRepository,atLeastOnce()).findById(1L);
        Assertions.assertThat(sctpConfigService.getClientLinksByStackId(1L)).isEqualTo(sigtranStack.getAssociations());
    }


    @Test
    public void saveSctpServe_Should_Return_SavedObject(){
        SctpServerConfig sctpServerConfig = new SctpServerConfig();
        sctpServerConfig.setId(1L);
        given(sctpServerRepository.save(sctpServerConfig)).willReturn(sctpServerConfig);
        ArgumentCaptor<SctpServerConfig> captorSctpServerConf = ArgumentCaptor.forClass(SctpServerConfig.class);
        sctpConfigService.addSctpServer(sctpServerConfig);
        verify(sctpServerRepository,atLeastOnce()).save(captorSctpServerConf.capture());

        Assertions.assertThat(captorSctpServerConf.getValue()).isEqualTo(sctpServerConfig);
        //TODO change asert if service will return something
    }

    @Test
    public void saveServerLink_Should_Return_SavedObject(){
        SctpServerAssociationConfig sctpServerAssociationConfig = new SctpServerAssociationConfig();
        sctpServerAssociationConfig.setId(1L);
        given(remoteSctpLinkRepository.save(sctpServerAssociationConfig)).willReturn(sctpServerAssociationConfig);
        ArgumentCaptor<SctpServerAssociationConfig> captorSctpServerAssociationConfig = ArgumentCaptor.forClass(SctpServerAssociationConfig.class); //todo change
        sctpConfigService.addServerLink(sctpServerAssociationConfig);
        verify(remoteSctpLinkRepository,atLeastOnce()).save(captorSctpServerAssociationConfig.capture());
        Assertions.assertThat(captorSctpServerAssociationConfig.getValue()).isEqualTo(sctpServerAssociationConfig);
        //TODO change asert if service will return something
    }

    @Test
    public void saveClinetLink_Should_Return_SavedObject(){
        SctpClientAssociationConfig sctpClientAssociationConfig = new SctpClientAssociationConfig();
        sctpClientAssociationConfig.setId(1L);
        given(sctpLinkRepository.save(sctpClientAssociationConfig)).willReturn(sctpClientAssociationConfig);
        ArgumentCaptor<SctpClientAssociationConfig> captorSctpClientAssociationConfig = ArgumentCaptor.forClass(SctpClientAssociationConfig.class); //todo change
        sctpConfigService.addClinetLink(sctpClientAssociationConfig);
        verify(sctpLinkRepository,atLeastOnce()).save(captorSctpClientAssociationConfig.capture());
        Assertions.assertThat(captorSctpClientAssociationConfig.getValue()).isEqualTo(sctpClientAssociationConfig);
        //TODO change asert if service will return something
    }
}
