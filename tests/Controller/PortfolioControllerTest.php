<?php

namespace App\Test\Controller;

use App\Entity\Portfolio;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\ORM\EntityRepository;
use Symfony\Bundle\FrameworkBundle\KernelBrowser;
use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;

class PortfolioControllerTest extends WebTestCase
{
    private KernelBrowser $client;
    private EntityManagerInterface $manager;
    private EntityRepository $repository;
    private string $path = '/portfolio/';

    protected function setUp(): void
    {
        $this->client = static::createClient();
        $this->manager = static::getContainer()->get('doctrine')->getManager();
        $this->repository = $this->manager->getRepository(Portfolio::class);

        foreach ($this->repository->findAll() as $object) {
            $this->manager->remove($object);
        }

        $this->manager->flush();
    }

    public function testIndex(): void
    {
        $crawler = $this->client->request('GET', $this->path);

        self::assertResponseStatusCodeSame(200);
        self::assertPageTitleContains('Portfolio index');

        // Use the $crawler to perform additional assertions e.g.
        // self::assertSame('Some text on the page', $crawler->filter('.p')->first());
    }

    public function testNew(): void
    {
        $this->markTestIncomplete();
        $this->client->request('GET', sprintf('%snew', $this->path));

        self::assertResponseStatusCodeSame(200);

        $this->client->submitForm('Save', [
            'portfolio[description]' => 'Testing',
            'portfolio[cv]' => 'Testing',
            'portfolio[image]' => 'Testing',
            'portfolio[iduser]' => 'Testing',
            'portfolio[idprojet]' => 'Testing',
            'portfolio[rating]' => 'Testing',
        ]);

        self::assertResponseRedirects('/sweet/food/');

        self::assertSame(1, $this->getRepository()->count([]));
    }

    public function testShow(): void
    {
        $this->markTestIncomplete();
        $fixture = new Portfolio();
        $fixture->setDescription('My Title');
        $fixture->setCv('My Title');
        $fixture->setImage('My Title');
        $fixture->setIduser('My Title');
        $fixture->setIdprojet('My Title');
        $fixture->setRating('My Title');

        $this->repository->save($fixture, true);

        $this->client->request('GET', sprintf('%s%s', $this->path, $fixture->getId()));

        self::assertResponseStatusCodeSame(200);
        self::assertPageTitleContains('Portfolio');

        // Use assertions to check that the properties are properly displayed.
    }

    public function testEdit(): void
    {
        $this->markTestIncomplete();
        $fixture = new Portfolio();
        $fixture->setDescription('Value');
        $fixture->setCv('Value');
        $fixture->setImage('Value');
        $fixture->setIduser('Value');
        $fixture->setIdprojet('Value');
        $fixture->setRating('Value');

        $this->manager->persist($fixture);
        $this->manager->flush();

        $this->client->request('GET', sprintf('%s%s/edit', $this->path, $fixture->getId()));

        $this->client->submitForm('Update', [
            'portfolio[description]' => 'Something New',
            'portfolio[cv]' => 'Something New',
            'portfolio[image]' => 'Something New',
            'portfolio[iduser]' => 'Something New',
            'portfolio[idprojet]' => 'Something New',
            'portfolio[rating]' => 'Something New',
        ]);

        self::assertResponseRedirects('/portfolio/');

        $fixture = $this->repository->findAll();

        self::assertSame('Something New', $fixture[0]->getDescription());
        self::assertSame('Something New', $fixture[0]->getCv());
        self::assertSame('Something New', $fixture[0]->getImage());
        self::assertSame('Something New', $fixture[0]->getIduser());
        self::assertSame('Something New', $fixture[0]->getIdprojet());
        self::assertSame('Something New', $fixture[0]->getRating());
    }

    public function testRemove(): void
    {
        $this->markTestIncomplete();
        $fixture = new Portfolio();
        $fixture->setDescription('Value');
        $fixture->setCv('Value');
        $fixture->setImage('Value');
        $fixture->setIduser('Value');
        $fixture->setIdprojet('Value');
        $fixture->setRating('Value');

        $$this->manager->remove($fixture);
        $this->manager->flush();

        $this->client->request('GET', sprintf('%s%s', $this->path, $fixture->getId()));
        $this->client->submitForm('Delete');

        self::assertResponseRedirects('/portfolio/');
        self::assertSame(0, $this->repository->count([]));
    }
}
