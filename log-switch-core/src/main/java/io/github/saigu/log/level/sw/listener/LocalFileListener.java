package io.github.saigu.log.level.sw.listener;

import io.github.saigu.log.level.sw.context.SwitchContext;
import io.github.saigu.log.level.sw.util.SwitchObjectMapperHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Function: 
 * 基于本地文件的实现
 * @author awan
 */
public class LocalFileListener extends AbstractConfigListener<String> {

    private static final Logger LOG = LoggerFactory.getLogger(LocalFileListener.class);

    private final ObjectMapper objectMapper = SwitchObjectMapperHolder.getSwitchObjectMapper();

    private static final String CONFIG_FILE_NAME = "LocalSwitch.json";

    private String path;

    private String directory;

    private FileAlterationListener fileAlterationListener;

    private FileAlterationMonitor fileMonitor;

    public LocalFileListener() {
        this.fileAlterationListener = new JsonFileListener();
        this.directory = LocalFileListener.class.getClassLoader().getResource("").getPath();
        this.path = LocalFileListener.class.getClassLoader().getResource(CONFIG_FILE_NAME)
                .getPath();

        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("no LocalSwitch.json found");
        }
        try {
            FileAlterationObserver observer = new FileAlterationObserver(new File(this.directory),
                    FileFilterUtils.and(FileFilterUtils.fileFileFilter(),
                            FileFilterUtils.nameFileFilter(CONFIG_FILE_NAME)));
            observer.addListener(this.fileAlterationListener);
            fileMonitor = new FileAlterationMonitor(3000, observer);
            fileMonitor.start();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public SwitchContext getInitSwitch() {
        try {
            String jsonContext = FileUtils.readFileToString(new File(this.path),
                    StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonContext, SwitchContext.class);

        } catch (IOException e) {
            LOG.error("read LocalSwitch.json error", e);
            throw new IllegalArgumentException("read LocalSwitch.json error");
        }
    }

    @Override
    public SwitchContext transferConfig(final String changedConfig) {
        SwitchContext newConfig = null;
        try {
            newConfig = objectMapper.readValue(changedConfig, SwitchContext.class);
            return newConfig;
        } catch (IOException e) {
            LOG.error("transfer LocalSwitch.json error: ", e);
            throw new IllegalArgumentException("transfer LocalSwitch.json error");
        }
    }

    class JsonFileListener extends FileAlterationListenerAdaptor {
        @Override
        public void onFileChange(final File file) {
            super.onFileChange(file);
            String jsonContext;
            try {
                jsonContext = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                listenChangedConfig(jsonContext);
            } catch (IOException e) {
                LOG.error("read LocalSwitch.json error", e);
                throw new IllegalArgumentException("read LocalSwitch.json error");
            }
        }
    }

}
