package com.fans.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fans.common.Maps;
import com.fans.mapper.CvslinkMapper;
import com.fans.mapper.GroupMapper;
import com.fans.mapper.LandingMapper;
import com.fans.mapper.WxgroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LandingImportTest {

    @Mock
    private LandingMapper landingMapper;
    @Mock
    private GroupMapper groupMapper;
    @Mock
    private WxgroupMapper wxgroupMapper;
    @Mock
    private CvslinkMapper cvslinkMapper;

    private LandingService service;

    @BeforeEach
    void setUp() {
        service = new LandingService(landingMapper, groupMapper, wxgroupMapper, cvslinkMapper,
                new Maps(new ObjectMapper()));
    }

    @Test
    @SuppressWarnings("unchecked")
    void previewClassifiesValidDuplicateAndInvalid() {
        when(landingMapper.selectList(any())).thenReturn(Collections.emptyList());
        Map<String, Object> data = new HashMap<>();
        data.put("landingurls", "https://a.test/1\nbad url\nhttps://a.test/1\nhttps://a.test/2");

        Map<String, Object> result = service.importPreview(data);
        Map<String, Object> payload = (Map<String, Object>) result.get("data");

        assertEquals(4, payload.get("total"));
        assertEquals(2, payload.get("validCount"));
        assertEquals(1, ((List<?>) payload.get("duplicates")).size());
        assertEquals(1, ((List<?>) payload.get("invalid")).size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void emptyInputIsRejected() {
        Map<String, Object> data = new HashMap<>();
        data.put("landingurls", "   ");

        Map<String, Object> result = service.importPreview(data);
        assertEquals(400, result.get("code"));
    }
}
