package com.vaults.test;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestNodeList implements NodeList {

    private final List<Node> nodeList;

    public TestNodeList(int size){
        nodeList = Stream
                .iterate(0, i -> i + 1)
                .limit(size)
                .map(i -> new IIOMetadataNode(UUID.randomUUID().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public Node item(int i) {
        return nodeList.get(i);
    }

    @Override
    public int getLength() {
        return nodeList.size();
    }

    public List<Node> getNodeList() {
        return nodeList;
    }
}
