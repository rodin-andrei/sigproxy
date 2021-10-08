package com.unifun.sigproxy.service.tcap;

import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.exception.SS7RemoveException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.tcap.TcapConfigRepository;
import com.unifun.sigproxy.service.tcap.impl.TcapConfigServiceImpl;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TcapServiceTest {


    @Mock
    private TcapConfigRepository tcapConfigRepository;

    @Mock
    private SigtranStackRepository sigtranStackRepository;


    private TcapConfigService tcapConfigService;

    @BeforeEach
    public void setUp(){
        this.tcapConfigService = new TcapConfigServiceImpl(tcapConfigRepository, sigtranStackRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testGetTcapConfigById_Return_Value(){
        TcapConfig tcapConfig = new TcapConfig();
        tcapConfig.setLocalSsn(1);
        int[] i = {2,3,4,5};
        tcapConfig.setAdditionalSsns(i);
        tcapConfig.setId(1L);
        given(tcapConfigRepository.findById(1L)).willReturn(Optional.of(tcapConfig));
        TcapConfig test = tcapConfigService.getTcapConfigById(1L);
        Assertions.assertThat(test).isNotNull();
        Assertions.assertThat(tcapConfigRepository.findById(1L)).isPresent();
    }

    @Test
    public void testGetTcapConfigByStackId__Throw_SS7NotFoundException(){
        //given
        Long id = 1L;
        //verify
        assertThatThrownBy(() -> tcapConfigService.getTcapConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    public void testGetTcapConfigByStackId_Return_Value(){
        //given
        Long id = 1L;
        SigtranStack sigtranStack = new SigtranStack();
        TcapConfig tcapConfig = new TcapConfig();
        sigtranStack.setTcapConfig(tcapConfig);
        given(sigtranStackRepository.findById(id)).willReturn(Optional.of(sigtranStack));

        //When
        tcapConfigService.getTcapConfigByStackId(id);

        //verify
        verify(sigtranStackRepository).findById(id);
        assertThat(tcapConfigService.getTcapConfigByStackId(id)).isEqualTo(tcapConfig);
    }

    @Test
    void testGetTcapConfigByStackId_Throw_SS7NotContentException() {
        //given
        Long id = 1L;
        given(sigtranStackRepository.findById(id)).willReturn(Optional.of(new SigtranStack()));

        //When
        //verify
        assertThatThrownBy(() -> tcapConfigService.getTcapConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
        verify(sigtranStackRepository).findById(id);

    }

    @Test
    void testGetTcapConfigByStackId_Throw_SS7NotFoundException() {
        //given
        Long id = 1L;

        //When
        //verify
        assertThatThrownBy(() -> tcapConfigService.getTcapConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);

    }

    @Test
    public void testRemoveTcap_Succes_Remove() {
        Long removeId = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        tcapConfigService.removeTcap(removeId);
        verify(tcapConfigRepository).deleteById(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(removeId);
    }


    @Test
    public void testRemoveTcap_SS7RemoveException() {
        Long id = 1L;
        doThrow(EmptyResultDataAccessException.class).when(tcapConfigRepository).deleteById(id);
        //when-verify
        assertThatThrownBy(() -> tcapConfigService.removeTcap(id)).isInstanceOf(SS7RemoveException.class);
    }

    @Test
    public void testAddTcapConfig_Succes_Save() {
        //given
        TcapConfig tcapConfig = new TcapConfig();
        given(tcapConfigRepository.save(tcapConfig)).willReturn(tcapConfig);
        ArgumentCaptor<TcapConfig> captor = ArgumentCaptor.forClass(TcapConfig.class);
        //when
        tcapConfigService.addTcapConfig(tcapConfig);

        //verify
        verify(tcapConfigRepository).save(captor.capture());
        AssertionsForClassTypes.assertThat(captor.getValue()).isEqualTo(tcapConfig);
        AssertionsForClassTypes.assertThat(tcapConfigService.addTcapConfig(tcapConfig)).isEqualTo(captor.getValue());

    }


}
