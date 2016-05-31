package com.salesforceiq.augmenteddriver.server.slack;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class SlackUtilTest {

    @Test
    public void garbageReturnsEmptyCommand() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("FDFDFD");
        Optional<String> command = SlackUtil.command(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void emptyMessageNoCommand() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("");
        Optional<String> command = SlackUtil.command(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void emptyUserReturnsEmptyCommand() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>");
        Optional<String> command = SlackUtil.command(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void noComandReturnsEmpty() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: ");
        Optional<String> command = SlackUtil.command(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void onlyCommandReturnsCommand() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: thecommand");
        Optional<String> command = SlackUtil.command(mock);
        Assert.assertEquals("thecommand", command.get());
    }

    @Test
    public void onlyCommandWithSpacesReturnsCommand() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: thecommand    ");
        Optional<String> command = SlackUtil.command(mock);
        Assert.assertEquals("thecommand", command.get());
    }

    @Test
    public void commandWithArgumentsReturnCommand() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: thecommand firstArg secondArgs ");
        Optional<String> command = SlackUtil.command(mock);
        Assert.assertEquals("thecommand", command.get());
    }

    @Test
    public void emptyMessageNoReceiver() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn(" ");
        Optional<String> command = SlackUtil.receiver(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void garbageMessageNoReceiver() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("FDSFSFDS");
        Optional<String> command = SlackUtil.receiver(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void oneSignNoReceiver() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<");
        Optional<String> command = SlackUtil.receiver(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void anotherSignNoReceiver() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn(">");
        Optional<String> command = SlackUtil.receiver(mock);
        Assert.assertTrue(!command.isPresent());
    }

    @Test
    public void onlyIdWorks() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>:");
        Optional<String> command = SlackUtil.receiver(mock);
        Assert.assertEquals("ID", command.get());
    }

    @Test
    public void idWithCommandsWork() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: command first arg");
        Optional<String> command = SlackUtil.receiver(mock);
        Assert.assertEquals("ID", command.get());
    }

    @Test
    public void emptyArgsWork() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: command ");
        Optional<List<String>> args = SlackUtil.arguments(mock);
        Assert.assertEquals(0, args.get().size());
    }

    @Test
    public void oneArgWorks() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: command oneArg ");
        Optional<List<String>> args = SlackUtil.arguments(mock);
        Assert.assertEquals(1, args.get().size());
        Assert.assertEquals("oneArg", args.get().get(0));
    }

    @Test
    public void twoArgsWork() {
        SlackMessagePosted mock = Mockito.mock(SlackMessagePosted.class);
        Mockito.when(mock.getMessageContent()).thenReturn("<@ID>: command oneArg   second");
        Optional<List<String>> args = SlackUtil.arguments(mock);
        Assert.assertEquals(2, args.get().size());
        Assert.assertEquals("oneArg", args.get().get(0));
        Assert.assertEquals("second", args.get().get(1));
    }
}
