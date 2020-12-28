package me.ixk.design_pattern.builder.outer_builder;

/**
 * 建造者
 *
 * @author Otstar Lin
 * @date 2020/12/23 下午 1:07
 */
public class ComputerBuilder {

    private Cpu cpu;
    private MainBoard mainBoard;
    private Memory memory;
    private Disk disk;

    public ComputerBuilder cpu(final String cpu) {
        this.cpu = new Cpu(cpu);
        return this;
    }

    public ComputerBuilder mainBoard(final String mainBoard) {
        this.mainBoard = new MainBoard(mainBoard);
        return this;
    }

    public ComputerBuilder memory(final String memory) {
        this.memory = new Memory(memory);
        return this;
    }

    public ComputerBuilder disk(final String disk) {
        this.disk = new Disk(disk);
        return this;
    }

    public static ComputerBuilder builder() {
        return new ComputerBuilder();
    }

    public Computer build() {
        if (cpu == null) {
            throw new IllegalArgumentException("Cpu not install");
        }
        if (mainBoard == null) {
            throw new IllegalArgumentException("MainBoard not install");
        }
        if (memory == null) {
            throw new IllegalArgumentException("Memory not install");
        }
        if (disk == null) {
            throw new IllegalArgumentException("Disk not install");
        }
        return new Computer(cpu, mainBoard, memory, disk);
    }
}
