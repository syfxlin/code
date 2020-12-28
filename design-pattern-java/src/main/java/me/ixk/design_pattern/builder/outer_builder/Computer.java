package me.ixk.design_pattern.builder.outer_builder;

/**
 * 要被建造的事物
 *
 * @author Otstar Lin
 * @date 2020/12/23 下午 1:05
 */
public class Computer {

    private final Cpu cpu;
    private final MainBoard mainBoard;
    private final Memory memory;
    private final Disk disk;

    public Computer(
        final Cpu cpu,
        final MainBoard mainBoard,
        final Memory memory,
        final Disk disk
    ) {
        this.cpu = cpu;
        this.mainBoard = mainBoard;
        this.memory = memory;
        this.disk = disk;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public MainBoard getMainBoard() {
        return mainBoard;
    }

    public Memory getMemory() {
        return memory;
    }

    public Disk getDisk() {
        return disk;
    }
}
