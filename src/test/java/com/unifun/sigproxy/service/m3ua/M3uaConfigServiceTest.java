package com.unifun.sigproxy.service.m3ua;

import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.AspRepository;
import com.unifun.sigproxy.repository.m3ua.RouteRepository;
import com.unifun.sigproxy.repository.m3ua.StackSettingsReposidory;
import com.unifun.sigproxy.service.m3ua.impl.M3uaConfigServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class M3uaConfigServiceTest {

    private  M3uaConfigService m3uaConfigService;

    @Mock
    private  AsRepository asRepository;

    @Mock
    private   StackSettingsReposidory stackSettingsReposidory;

    @Mock
    private AspRepository aspRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private SigtranStackRepository sigtranStackRepository;


    @BeforeEach
    public void setUp() {
        this.m3uaConfigService = new M3uaConfigServiceImpl(asRepository,stackSettingsReposidory,aspRepository,routeRepository,sigtranStackRepository);
    }


    @Test
    public void testGetM3uaAsConfigByStackIdNotNull() {
        //given
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        m3uaAsConfig.setId(1L);
        given(asRepository.findById(1L)).willReturn(Optional.of(m3uaAsConfig));

        //when
        m3uaConfigService.getM3uaAsConfigById(1L);

        //verify
        verify(asRepository).findById(1L);
        Assertions.assertThat(m3uaConfigService.getM3uaAsConfigById(1L)).isEqualTo(m3uaAsConfig);
    }

    @Test
    public void testGetM3uaAsConfigByStackIdShouldReturnNull() {
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaAsConfigById(1L)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    public void testGetM3uaAspConfigByAsId_Return_Value() {
        //given
        Set<M3uaAspConfig> m3uaAspConfigSet = new HashSet<>();
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        m3uaAsConfig.setApplicationServerPoints(m3uaAspConfigSet);

        given(asRepository.findById(1L)).willReturn(Optional.of(m3uaAsConfig));

        //when
        m3uaConfigService.getM3uaAspConfigByAsId(1L);

        //verify
        verify(asRepository).findById(1L);
        assertThat(m3uaConfigService.getM3uaAspConfigByAsId(1L)).isEqualTo(m3uaAspConfigSet);

    }

    @Test
    public void testGetM3uaAspConfigByStackId_Return_SS7NotFoundException() {
        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaAspConfigByAsId(1L)).isInstanceOf(SS7NotFoundException.class);

    }
    @Test
    public void testGetM3uaAspConfigByStackId_Return_SS7NotContentException() {
        //given
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        given(asRepository.findById(1L)).willReturn(Optional.of(m3uaAsConfig));

        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaAspConfigByAsId(1L)).isInstanceOf(SS7NotContentException.class);
    }

    @Test
    public void testGetM3uaRouteConfigByM3UaAsId_Return_SetValue() {
        //given
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
//        Set<M3uaRouteConfig> m3uaRouteConfigSet = new HashSet<>();
        m3uaAsConfig.setRoutes(new HashSet<M3uaRouteConfig>());
        given(asRepository.findById(1L)).willReturn(Optional.of(m3uaAsConfig));

        //when
        m3uaConfigService.getM3uaRouteConfigByAsId(1L);

        //verify
        verify(asRepository).findById(1L);
        assertThat(m3uaConfigService.getM3uaRouteConfigByAsId(1L)).isEqualTo(m3uaAsConfig.getRoutes());
    }

    @Test
    public void testGetM3uaRouteConfigByM3UaAsId_Return_SS7NotContentException() {
        //given
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        m3uaAsConfig.setId(1L);
        given(asRepository.findById(1L)).willReturn(Optional.of(m3uaAsConfig));

        //when
        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaRouteConfigByAsId(1L)).isInstanceOf(SS7NotContentException.class);
    }

    @Test
    public void testGetM3uaRouteConfigByM3UaAsId_Return_SS7NotFoundException() {
        //when
        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaAsConfigByStackId(1L)).isInstanceOf(SS7NotFoundException.class);
    }


    @Test
    public void testGetM3uaStackSettingsConfigByStackId_Return_Value() {
        //given
        M3uaStackSettingsConfig m3uaStackSettingsConfig = new M3uaStackSettingsConfig();
        SigtranStack sigtranStack = new SigtranStack();
        sigtranStack.setM3UaStackSettingsConfig(m3uaStackSettingsConfig);
        given(sigtranStackRepository.findById(1L)).willReturn(Optional.of(sigtranStack));

        //when
        m3uaConfigService.getM3uaStackSettingsConfigByStackId(1L);

        //verify
        verify(sigtranStackRepository).findById(1L);
        assertThat(m3uaConfigService.getM3uaStackSettingsConfigByStackId(1L)).isEqualTo(m3uaStackSettingsConfig);
    }

    @Test
    public void testGetM3uaStackSettingsConfigByStackId_Return_SS7NotContentException() {
        //given
        Long id = 1L;
        given(sigtranStackRepository.findById(id)).willReturn(Optional.of(new SigtranStack()));

        //when
        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaStackSettingsConfigByStackId(id)).isInstanceOf(SS7NotContentException.class);
    }


    @Test
    public void testGetM3uaStackSettingsConfigByStackId_Return_SS7NotFoundException() {
        Long id = 1L;
        //when
        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaStackSettingsConfigByStackId(id)).isInstanceOf(SS7NotFoundException.class);
    }


    @Test
    public void testGetM3uaAsConfigById_Return_Value() {
        Long id = 1L;
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        //given
        given(asRepository.findById(id)).willReturn(Optional.of(m3uaAsConfig));

        //when
        m3uaConfigService.getM3uaAsConfigById(id);

        //verify
        verify(asRepository).findById(id);
        assertThat( m3uaConfigService.getM3uaAsConfigById(id)).isEqualTo(m3uaAsConfig);
    }

    @Test
    public void testGetM3uaAsConfigById_Return_SS7NotFoundException() {
        Long id = 1L;
        //when
        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaAsConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }


    @Test
    public void testGetM3uaAspConfigById_Return_Value() {
        Long id = 1L;
        M3uaAspConfig m3uaAspConfig = new M3uaAspConfig();

        //given
        given(aspRepository.findById(id)).willReturn(Optional.of(m3uaAspConfig));

        //when
        m3uaConfigService.getM3uaAspConfigById(id);

        //verify
        verify(aspRepository).findById(id);
        assertThat( m3uaConfigService.getM3uaAspConfigById(id)).isEqualTo(m3uaAspConfig);
    }

    @Test
    public void testGetM3uaAspConfigById_Return_SS7NotFoundException() {
        Long id = 1L;
        //when
        //verify
        Assertions.assertThatThrownBy(()->m3uaConfigService.getM3uaAspConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }


    @Test
    public void testGetM3uaRouteConfigById_Return_Value() {
        Long id = 1L;
        //given
        M3uaRouteConfig m3uaRouteConfig = new M3uaRouteConfig();
        given(routeRepository.findById(id)).willReturn(Optional.of(m3uaRouteConfig));

        //when
        m3uaConfigService.getM3uaRouteConfigById(id);

        //verify
        verify(routeRepository).findById(id);
        assertThat( m3uaConfigService.getM3uaRouteConfigById(id)).isEqualTo(m3uaRouteConfig);

    }

    @Test
    public void testGetM3uaRouteConfigById_Return_SS7NotFoundException() {
        //given
        Long id = 1L;

        //when
        //verify
        assertThatThrownBy(()->m3uaConfigService.getM3uaRouteConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    public void testGet3uaStackSettingsConfigById_Return_Value() {
        Long id = 1L;
        //given
        M3uaStackSettingsConfig m3uaStackSettingsConfig = new M3uaStackSettingsConfig();
        given(stackSettingsReposidory.findById(id)).willReturn(Optional.of(m3uaStackSettingsConfig));

        //when
        m3uaConfigService.getM3uaStackSettingsConfigById(id);

        //verify
        verify(stackSettingsReposidory).findById(id);
        assertThat( m3uaConfigService.getM3uaStackSettingsConfigById(id)).isEqualTo( m3uaStackSettingsConfig);
    }

    @Test
    public void testGet3uaStackSettingsConfigById_Return_SS7NotFoundException() {
        //given
        Long id = 1L;

        //when
        //verify
        assertThatThrownBy(()->m3uaConfigService.getM3uaStackSettingsConfigById(id)).isInstanceOf(SS7NotFoundException.class);
    }

    @Test
    public void testAddM3uaAsConfig_Succes_Save() {
        //given
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        given(asRepository.save(m3uaAsConfig)).willReturn(m3uaAsConfig);
        ArgumentCaptor<M3uaAsConfig> captor = ArgumentCaptor.forClass(M3uaAsConfig.class);
        //when
        m3uaConfigService.addM3uaAsConfig(m3uaAsConfig);

        //verify
        verify(asRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(m3uaAsConfig);
        assertThat(m3uaConfigService.addM3uaAsConfig(m3uaAsConfig)).isEqualTo(captor.getValue());
    }



    @Test
    public void testAddM3uaStackSettingsConfig_Succes_Save() {
        //given
        M3uaStackSettingsConfig m3uaStackSettingsConfig = new  M3uaStackSettingsConfig();
        given(stackSettingsReposidory.save(m3uaStackSettingsConfig)).willReturn(m3uaStackSettingsConfig);
        ArgumentCaptor<M3uaStackSettingsConfig> captor = ArgumentCaptor.forClass(M3uaStackSettingsConfig.class);
        //when
        m3uaConfigService.addM3uaStackSettingsConfig(m3uaStackSettingsConfig);

        //verify
        verify(stackSettingsReposidory).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(m3uaStackSettingsConfig);
        assertThat(m3uaConfigService.addM3uaStackSettingsConfig(m3uaStackSettingsConfig)).isEqualTo(captor.getValue());
    }

    @Test
    public void testAddM3uaAspConfig_Succes_Save() {
        //given
        M3uaAspConfig m3uaAspConfig = new M3uaAspConfig();
        given(aspRepository.save(m3uaAspConfig)).willReturn(m3uaAspConfig);
        ArgumentCaptor<M3uaAspConfig> captor = ArgumentCaptor.forClass(M3uaAspConfig.class);
        //when
        m3uaConfigService.addM3uaAspConfig(m3uaAspConfig);

        //verify
        verify(aspRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(m3uaAspConfig);
        assertThat(m3uaConfigService.addM3uaAspConfig(m3uaAspConfig)).isEqualTo(captor.getValue());

    }

    @Test
    public void testAddM3uaRouteConfig_Succes_Save() {
        //give
        M3uaRouteConfig m3uaRouteConfig = new M3uaRouteConfig();
        given(routeRepository.save(m3uaRouteConfig)).willReturn(m3uaRouteConfig);
        ArgumentCaptor<M3uaRouteConfig> captor = ArgumentCaptor.forClass(M3uaRouteConfig.class);

        //when
        m3uaConfigService.addM3uaRouteConfig(m3uaRouteConfig);

        //verify
        verify(routeRepository).save(captor.capture());
        assertThat(m3uaConfigService.addM3uaRouteConfig(m3uaRouteConfig)).isEqualTo(m3uaRouteConfig);

    }

    @Test
    public void testRemoveM3uaAsConfig() {
        Long deletedId = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        m3uaConfigService.removeM3uaAsConfig(deletedId);
        verify(asRepository).deleteById(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(deletedId);
    }

    @Test
    public void testRemoveM3uaStackSettingsConfig() {
        Long deletedId = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        m3uaConfigService.removeM3uaStackSettingsConfig(deletedId);
        verify(stackSettingsReposidory).deleteById(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(deletedId);
    }

    @Test
    public void testRemoveM3uaAspConfig() {
        Long deletedId = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        m3uaConfigService.removeM3uaAspConfig(deletedId);
        verify(aspRepository).deleteById(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(deletedId);
    }

    @Test
    public void testRemoveM3uaRouteConfig() {
        Long deletedId = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        m3uaConfigService.removeM3uaRouteConfig(deletedId);
        verify(routeRepository).deleteById(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(deletedId);
    }
}
